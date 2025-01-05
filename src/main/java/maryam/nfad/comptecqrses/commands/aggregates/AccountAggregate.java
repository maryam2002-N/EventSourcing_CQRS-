package maryam.nfad.comptecqrses.commands.aggregates;

import maryam.nfad.comptecqrses.commonApi.commands.CreateAccountCommand;
import maryam.nfad.comptecqrses.commonApi.commands.CreditAccountCommand;
import maryam.nfad.comptecqrses.commonApi.commands.DebitAccountCommand;
import maryam.nfad.comptecqrses.commonApi.enums.AccountStatus;
import maryam.nfad.comptecqrses.commonApi.events.AccountActivatedEvent;
import maryam.nfad.comptecqrses.commonApi.events.AccountCreatedEvent;
import maryam.nfad.comptecqrses.commonApi.events.AccountCreditedEvent;
import maryam.nfad.comptecqrses.commonApi.events.AccountDebitedEvent;
import maryam.nfad.comptecqrses.commonApi.exceptions.AmountNegativeException;
import maryam.nfad.comptecqrses.commonApi.exceptions.BalanceNotSuffcientException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {


    @AggregateIdentifier //Affecter automatiquement
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate(){
        // Required by AXON
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if (createAccountCommand.getInitialBalance() < 0) throw new RuntimeException("Impossible ... ");
        //OK
        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED
        ));
    }



    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId=event.getId();
        this.balance=event.getInitialBalance();
        this.currency=event.getCurrency();
        this.status=AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));

    }
    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        this.status=event.getStatus();

    }
    @CommandHandler
    public void handler(CreditAccountCommand command){
        if(command.getAmount()<0) throw new AmountNegativeException("Amount should not be negative");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()

        ));
    }
    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance += event.getAmount();
    }


    @CommandHandler
    public void handler(DebitAccountCommand command){
        if(command.getAmount()<0) throw new AmountNegativeException("Amount should not be negative");
        if(this.balance<command.getAmount()) throw new BalanceNotSuffcientException("Balance not sufficient => "+balance);
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()

        ));
    }
    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance -= event.getAmount();
    }

}

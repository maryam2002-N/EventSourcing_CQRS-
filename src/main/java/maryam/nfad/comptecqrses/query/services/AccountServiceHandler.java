package maryam.nfad.comptecqrses.query.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maryam.nfad.comptecqrses.commonApi.enums.OperationType;
import maryam.nfad.comptecqrses.commonApi.events.AccountActivatedEvent;
import maryam.nfad.comptecqrses.commonApi.events.AccountCreatedEvent;
import maryam.nfad.comptecqrses.commonApi.events.AccountCreditedEvent;
import maryam.nfad.comptecqrses.commonApi.events.AccountDebitedEvent;
import maryam.nfad.comptecqrses.commonApi.queries.GetAccountByIdQuery;
import maryam.nfad.comptecqrses.commonApi.queries.GetAllAccountsQuery;
import maryam.nfad.comptecqrses.query.entities.Account;
import maryam.nfad.comptecqrses.query.entities.Operation;
import maryam.nfad.comptecqrses.query.repositories.AccountRepository;
import maryam.nfad.comptecqrses.query.repositories.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountServiceHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;
    @EventHandler
    public void on(AccountCreatedEvent event){
        log.info("*****************************************");
        log.info("accountCreatedEvent received");
        Account account=new Account();
        account.setId(event.getId());
        account.setCurrency(event.getCurrency());
        account.setStatus(event.getStatus());
        account.setBalance(event.getInitialBalance());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event){
        log.info("*****************************************");
        log.info("AccountActivatedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event){
        log.info("*****************************************");
        log.info("AccountActivatedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date()); // à ne pas faire , normalement il faut prendre la date de l'execution de l'operation c'est pas la date du moment de lecture
        operation.setType(OperationType.DEBIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepository.save(account);
    }
    @EventHandler
    public void on(AccountCreditedEvent event){
        log.info("*****************************************");
        log.info("AccountActivatedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date()); // à ne pas faire , normalement il faut prendre la date de l'execution de l'operation c'est pas la date du moment de lecture
        operation.setType(OperationType.CREDIT );
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query){
        return accountRepository.findAll();
    }


    @QueryHandler
    public Account on(GetAccountByIdQuery query){
        return accountRepository.findById(query.getId()).get();
    }


}

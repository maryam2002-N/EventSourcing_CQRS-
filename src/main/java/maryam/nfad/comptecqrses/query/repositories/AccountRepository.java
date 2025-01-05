package maryam.nfad.comptecqrses.query.repositories;

import maryam.nfad.comptecqrses.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}

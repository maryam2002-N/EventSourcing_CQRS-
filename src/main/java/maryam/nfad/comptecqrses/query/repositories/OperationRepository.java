package maryam.nfad.comptecqrses.query.repositories;

import maryam.nfad.comptecqrses.query.entities.Account;
import maryam.nfad.comptecqrses.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation,Long> {
}

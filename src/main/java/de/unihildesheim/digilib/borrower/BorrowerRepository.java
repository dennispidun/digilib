package de.unihildesheim.digilib.borrower;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    Optional<Borrower> findByFirstnameAndLastname(String firstname, String lastname);

}

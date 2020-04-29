package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.borrowing.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface BorrowerRepository extends PagingAndSortingRepository<Borrower, Long> {

    Optional<Borrower> findByFirstnameAndLastname(String firstname, String lastname);


}

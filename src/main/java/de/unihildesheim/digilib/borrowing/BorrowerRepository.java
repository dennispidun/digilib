package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.borrowing.model.Borrower;
import de.unihildesheim.digilib.borrowing.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface BorrowerRepository extends PagingAndSortingRepository<Borrower, Long> {

    Optional<Borrower> findByFirstnameAndLastname(String firstname, String lastname);
    

    List<Borrower> findAllByFirstnameIgnoreCaseContainingOrLastnameIgnoreCaseContaining(String firstname, String lastname);

    // JPQL
    @Query(value = "SELECT b.firstname, (SELECT count(*) FROM Borrowing WHERE borrower_id = b.id) as unreturned FROM Borrower as b", nativeQuery = true)
    List<ListBorrowerDto> findAllBorrower();

}

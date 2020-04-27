package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.borrowing.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    List<Borrowing> getBorrowingByBook_InvnrOrderByBorrowedOnDesc(String book_invnr);
    List<Borrowing> getBorrowingByBorrower_IdAndReturnedOnIsNull(Long Id);

}

package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.borrower.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    List<Borrowing> getBorrowingByBook_InvnrOrderByBorrowedOnDesc(String book_invnr);
    List<Borrowing> getBorrowingByBorrower_IdAndReturnedOnIsNull(Long Id);

}

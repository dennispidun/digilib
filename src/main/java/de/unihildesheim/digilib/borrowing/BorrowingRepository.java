package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.borrowing.model.Borrowing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public
interface BorrowingRepository extends PagingAndSortingRepository<Borrowing, Long> {

    List<Borrowing> getBorrowingByBook_InvnrOrderByBorrowedOnDesc(String book_invnr);
    List<Borrowing> getBorrowingByBorrower_IdAndReturnedOnIsNull(Long Id);
    List<Borrowing> getBorrowingByReturnedOn(Date returnedOn);

    Page<Borrowing> findAllByShouldReturnOnAfterAndShouldReturnOnBeforeAndReturnedOnIsNull(Pageable pageable, LocalDate lower, LocalDate upper);
    Page<Borrowing> findAllByShouldReturnOnBeforeAndReturnedOnIsNull(Pageable pageable, LocalDate lower);

}

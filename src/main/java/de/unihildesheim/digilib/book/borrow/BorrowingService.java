package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.book.*;
import de.unihildesheim.digilib.student.Borrower;
import de.unihildesheim.digilib.student.BorrowerRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowingService {

    private BorrowingRepository repository;

    private BookRepository bookRepository;

    private BorrowerRepository borrowerRepository;

    public BorrowingService(BorrowingRepository repository, BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    public Borrowing borrow(CreateBorrowingDto createBorrowing, String invnr) throws BookAlreadyBorrowedException {
        try {
            BorrowingsDto latestBorrowing = getLatestBorrowing(invnr);
            if (latestBorrowing.getBorrowedOn() != null && latestBorrowing.getReturnedOn() == null) {
                throw new BookAlreadyBorrowedException();
            }
        } catch (BookNeverBorrowedException e) {
        }

        Optional<Borrower> byFirstnameAndLastname = borrowerRepository.
                findByFirstnameAndLastname(createBorrowing.getFirstname(), createBorrowing.getLastname());

        Book book = bookRepository.findBookByInvnr(invnr).orElseThrow(() -> new BookNotFoundException());
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowedOn(new Date());
        borrowing.setBook(book);

        if (byFirstnameAndLastname.isEmpty()) {
            Borrower borrower = new Borrower();
            borrower.setFirstname(createBorrowing.getFirstname());
            borrower.setLastname(createBorrowing.getLastname());

            borrowerRepository.save(borrower);
            borrowing.setBorrower(borrower);
        } else {
            borrowing.setBorrower(byFirstnameAndLastname.get());
        }

        return repository.save(borrowing);
    }

    public BorrowingsDto getLatestBorrowing(String invnr) {
        return getBorrowings(invnr).stream().findFirst().orElseThrow(() -> new BookNeverBorrowedException());
    }

    public List<BorrowingsDto> getBorrowings(String invnr) {
        return repository.getBorrowingByBook_InvnrOrderByBorrowedOnDesc(invnr).stream().map(borrowing -> {
            BorrowingsDto borrowingDto = new BorrowingsDto();
            borrowingDto.setBorrower(borrowing.getBorrower());
            borrowingDto.setBorrowedOn(borrowing.getBorrowedOn());
            borrowingDto.setReturnedOn(borrowing.getReturnedOn());
            return borrowingDto;
        }).collect(Collectors.toList());
    }

    public BorrowingsDto cancelLatestBorrowing(String invnr) {
        Borrowing borrowing = repository.getBorrowingByBook_InvnrOrderByBorrowedOnDesc(invnr)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BookNeverBorrowedException());
        borrowing.setReturnedOn(new Date());

        repository.save(borrowing);

        BorrowingsDto borrowingDto = new BorrowingsDto();
        borrowingDto.setBorrower(borrowing.getBorrower());
        borrowingDto.setBorrowedOn(borrowing.getBorrowedOn());
        borrowingDto.setReturnedOn(borrowing.getReturnedOn());

        return borrowingDto;
    }
}

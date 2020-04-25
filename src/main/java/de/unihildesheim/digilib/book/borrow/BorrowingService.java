package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.book.*;
import de.unihildesheim.digilib.borrower.Borrower;
import de.unihildesheim.digilib.borrower.BorrowerRepository;
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
                throw new BookAlreadyBorrowedException(invnr);
            }

        } catch (BookNeverBorrowedException e) {

        }

        Optional<Borrower> byFirstnameAndLastname = borrowerRepository.
                findByFirstnameAndLastname(createBorrowing.getFirstname(), createBorrowing.getLastname());

        byFirstnameAndLastname.ifPresent(
                (borrower) -> {
                    try {
                        if (!(borrower.isTeacher()) && !(getUnreturnedBorrowings(borrower).isEmpty())) {
                            throw new StudentCannotBorrowMultipleBooks();
                        }
                    } catch (StudentCannotBorrowMultipleBooks e) {
                        // do some stuff
                    }
                }
        );

        Book book = bookRepository.findBookByInvnr(invnr).orElseThrow(() -> new BookNotFoundException(invnr));
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowedOn(new Date());
        borrowing.setBook(book);

        byFirstnameAndLastname.ifPresentOrElse(
                (borrower) -> {
                    borrowing.setBorrower(borrower);
                },
                () -> {
                    Borrower borrower = new Borrower();
                    borrower.setFirstname(createBorrowing.getFirstname());
                    borrower.setLastname(createBorrowing.getLastname());

                    borrowerRepository.save(borrower);
                    borrowing.setBorrower(borrower);
                }
        );

        return repository.save(borrowing);
    }

    public BorrowingsDto getLatestBorrowing(String invnr) {
        return getBorrowings(invnr).stream().findFirst().orElseThrow(() -> new BookNeverBorrowedException(invnr));
    }

    public List<BorrowingsDto> getUnreturnedBorrowings(Borrower borrower) {
        return repository.getBorrowingByBorrower_IdAndReturnedOnIsNull(borrower.getId()).stream().map(borrowing -> {
            BorrowingsDto borrowingDto = new BorrowingsDto();
            borrowingDto.setBorrower(borrowing.getBorrower());
            borrowingDto.setBorrowedOn(borrowing.getBorrowedOn());
            borrowingDto.setReturnedOn(borrowing.getReturnedOn());
            return borrowingDto;
        }).collect(Collectors.toList());
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
                .orElseThrow(() -> new BookNeverBorrowedException(invnr));
        borrowing.setReturnedOn(new Date());

        repository.save(borrowing);

        BorrowingsDto borrowingDto = new BorrowingsDto();
        borrowingDto.setBorrower(borrowing.getBorrower());
        borrowingDto.setBorrowedOn(borrowing.getBorrowedOn());
        borrowingDto.setReturnedOn(borrowing.getReturnedOn());

        return borrowingDto;
    }
}

package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.book.*;
import de.unihildesheim.digilib.book.BookNotFoundException;
import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.book.model.ListBookDto;
import de.unihildesheim.digilib.borrowing.model.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public
class BorrowingService {

    private BorrowingRepository repository;

    private BookRepository bookRepository;

    private BorrowerRepository borrowerRepository;

    private BorrowingModelMapper borrowingModelMapper;

    public BorrowingService(BorrowingRepository repository, BookRepository bookRepository, BorrowerRepository borrowerRepository, BorrowingModelMapper borrowingModelMapper) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
        this.borrowingModelMapper = borrowingModelMapper;
    }

    public Borrowing borrow(CreateBorrowingDto createBorrowing, String invnr) throws BookAlreadyBorrowedException,
            StudentCannotBorrowMultipleBooks {
        checkIfBorrowed(invnr);

        Optional<Borrower> byFirstnameAndLastname = borrowerRepository.
                findByFirstnameAndLastname(createBorrowing.getFirstname(), createBorrowing.getLastname());

        byFirstnameAndLastname.ifPresent(borrower -> {
                if (!(borrower.isTeacher()) && !(getUnreturnedBorrowings(borrower).isEmpty())) {
                    throw new StudentCannotBorrowMultipleBooks();
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

    public ListBookDto addBorrowHistory(ListBookDto book) {
        try {
            BorrowingDto latestBorrowing = getLatestBorrowing(book.getInvnr());
            if (latestBorrowing.getReturnedOn() == null && latestBorrowing.getBorrowedOn() != null) {
                book.setBorrowedOn(latestBorrowing.getBorrowedOn());
                book.setBorrowerName(latestBorrowing.getBorrower().getFirstname() + " " + latestBorrowing.getBorrower().getLastname());
            }
        } catch (Exception e) {
        }
        return book;
    }

    private void checkIfBorrowed(String invnr) {
        try {
            BorrowingDto latestBorrowing = getLatestBorrowing(invnr);
            if (latestBorrowing.getBorrowedOn() != null && latestBorrowing.getReturnedOn() == null) {
                throw new BookAlreadyBorrowedException(invnr);
            }

        } catch (BookNeverBorrowedException e) {

        }
    }

    public BorrowingDto getLatestBorrowing(String invnr) {
        return getBorrowings(invnr).stream().findFirst().orElseThrow(() -> new BookNeverBorrowedException(invnr));
    }

    public List<BorrowingDto> getUnreturnedBorrowings(Borrower borrower) {
        return repository.getBorrowingByBorrower_IdAndReturnedOnIsNull(borrower.getId())
                .stream()
                .map(borrowingModelMapper::mapToListBorrowing)
                .collect(Collectors.toList());
    }

    public List<BorrowingDto> getBorrowings(String invnr) {
        return repository.getBorrowingByBook_InvnrOrderByBorrowedOnDesc(invnr)
                .stream()
                .map(borrowingModelMapper::mapToListBorrowing)
                .collect(Collectors.toList());
    }

    public BorrowingDto cancelLatestBorrowing(String invnr) {
        Borrowing borrowing = repository.getBorrowingByBook_InvnrOrderByBorrowedOnDesc(invnr)
                .stream()
                .findFirst()
                .orElseThrow(() -> new BookNeverBorrowedException(invnr));
        borrowing.setReturnedOn(new Date());

        repository.save(borrowing);

        return borrowingModelMapper.mapToListBorrowing(borrowing);
    }
}

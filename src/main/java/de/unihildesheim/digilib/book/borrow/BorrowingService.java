package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.book.*;
import de.unihildesheim.digilib.student.Student;
import de.unihildesheim.digilib.student.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowingService {

    private BorrowingRepository repository;

    private BookRepository bookRepository;

    private StudentRepository studentRepository;

    public BorrowingService(BorrowingRepository repository, BookRepository bookRepository, StudentRepository studentRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    public Borrowing borrow(CreateBorrowingDto createBorrowing, String invnr) throws BookAlreadyBorrowedException {
        try {
            BorrowingsDto latestBorrowing = getLatestBorrowing(invnr);
            if (latestBorrowing.getBorrowedOn() != null && latestBorrowing.getReturnedOn() == null) {
                throw new BookAlreadyBorrowedException();
            }
        } catch (BookNeverBorrowedException e) {
        }

        Optional<Student> byFirstnameAndLastname = studentRepository.
                findByFirstnameAndLastname(createBorrowing.getFirstname(), createBorrowing.getLastname());

        Book book = bookRepository.findBookByInvnr(invnr).orElseThrow(() -> new BookNotFoundException());
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowedOn(new Date());
        borrowing.setBook(book);

        if (byFirstnameAndLastname.isEmpty()) {
            Student student = new Student();
            student.setFirstname(createBorrowing.getFirstname());
            student.setLastname(createBorrowing.getLastname());

            studentRepository.save(student);
            borrowing.setStudent(student);
        } else {
            borrowing.setStudent(byFirstnameAndLastname.get());
        }

        return repository.save(borrowing);
    }

    public BorrowingsDto getLatestBorrowing(String invnr) {
        return getBorrowings(invnr).stream().findFirst().orElseThrow(() -> new BookNeverBorrowedException());
    }

    public List<BorrowingsDto> getBorrowings(String invnr) {
        return repository.getBorrowingByBook_InvnrOrderByBorrowedOnDesc(invnr).stream().map(borrowing -> {
            BorrowingsDto borrowingDto = new BorrowingsDto();
            borrowingDto.setStudent(borrowing.getStudent());
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
        borrowingDto.setStudent(borrowing.getStudent());
        borrowingDto.setBorrowedOn(borrowing.getBorrowedOn());
        borrowingDto.setReturnedOn(borrowing.getReturnedOn());

        return borrowingDto;
    }
}

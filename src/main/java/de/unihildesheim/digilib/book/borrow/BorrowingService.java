package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.book.*;
import de.unihildesheim.digilib.student.Student;
import de.unihildesheim.digilib.student.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    public Borrowing borrow(CreateBorrowingDto createBorrowing, String invnr) {
        Book book = bookRepository.findBookByInvnr(invnr).orElseThrow(() -> new BookNotFoundException());

        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowedOn(new Date());
        borrowing.setBook(book);

        Student student = new Student();
        student.setSurname(createBorrowing.getSurname());
        student.setLastname(createBorrowing.getLastname());

        studentRepository.save(student);

        borrowing.setStudent(student);

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
            return borrowingDto;
        }).collect(Collectors.toList());
    }
}

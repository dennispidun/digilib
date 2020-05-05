package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.book.model.BookDto;
import de.unihildesheim.digilib.book.model.ListBookDto;
import de.unihildesheim.digilib.borrowing.BorrowingRepository;
import de.unihildesheim.digilib.utils.ISBNUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksProvider {

    public static final int BOOKS_BEHIND_DAYS = 1;
    private BookRepository repository;
    private BorrowingRepository borrowingRepository;

    public BooksProvider(BookRepository repository, BorrowingRepository borrowingRepository) {
        this.repository = repository;
        this.borrowingRepository = borrowingRepository;
    }

    public Book create(BookDto createBook) {
        Book book = new Book();
        book.setAuthor(createBook.getAuthor());
        book.setTitle(createBook.getTitle());
        book.setInvnr(createBook.getInvnr());
        book.setIsbn(ISBNUtils.regenerateISBN(createBook.getIsbn()));

        book.setCreatedOn(createBook.getCreatedOn());
        if (createBook.getCreatedOn() == null) {
            book.setCreatedOn(new Date());
        }
        return repository.save(book);
    }

    public Page<ListBookDto> searchForPaginated(String search, int pageNo, int pageSize) {
        return repository
                .findBooksByInvnrContainingOrIsbnContainingOrTitleContainingOrAuthorContaining(
                        search, search, search, search, PageRequest.of(pageNo, pageSize))
                .map(book -> new ListBookDto(book, null));
    }

    public Page<ListBookDto> findPaginated(int pageNo, int pageSize) {
        return repository.findAll(PageRequest.of(pageNo, pageSize))
                .map(book -> new ListBookDto(book, null));
    }

    public Page<ListBookDto> findBehindPaginated(int pageNo, int pageSize) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -BOOKS_BEHIND_DAYS);
        Date thendate = cal.getTime();

        return borrowingRepository.findAllByBorrowedOnBeforeAndReturnedOnIsNull(PageRequest.of(pageNo, pageSize), thendate)
                .map(borrowing -> new ListBookDto(borrowing.getBook(), borrowing.getBorrowedOn()));
    }
}

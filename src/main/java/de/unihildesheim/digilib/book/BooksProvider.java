package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.book.model.BookDto;
import de.unihildesheim.digilib.book.model.ListBookDto;
import de.unihildesheim.digilib.borrowing.BorrowingRepository;
import de.unihildesheim.digilib.genre.GenreProvider;
import de.unihildesheim.digilib.utils.ISBNUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Date;

@Service
public class BooksProvider {

    private BookRepository repository;
    private BorrowingRepository borrowingRepository;

    private GenreProvider genreProvider;

    public BooksProvider(BookRepository repository,
                         BorrowingRepository borrowingRepository,
                         GenreProvider genreProvider) {
        this.repository = repository;
        this.borrowingRepository = borrowingRepository;
        this.genreProvider = genreProvider;
    }

    public Book create(BookDto createBook) {
        Book book = new Book();
        book.setAuthor(createBook.getAuthor());
        book.setTitle(createBook.getTitle());
        book.setInvnr(createBook.getInvnr());
        book.setIsbn(ISBNUtils.regenerateISBN(createBook.getIsbn()));
        book.setGenre(genreProvider.getOrSave(createBook.getGenre()));
        book.setType(createBook.getType());
        book.setComment(createBook.getComment());
        book.setPrice(createBook.getPrice());

        book.setCreatedOn(createBook.getCreatedOn());
        if (createBook.getCreatedOn() == null) {
            book.setCreatedOn(new Date());
        }
        return repository.save(book);
    }

    public Page<ListBookDto> searchForPaginated(String search, @Min(1) int pageNo, int pageSize) {
        return repository
                .findBooksByInvnrIgnoreCaseContainingOrIsbnContainingOrTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndDeletedOnIsNull(
                        search, search, search, search, PageRequest.of(pageNo, pageSize))
                .map(book -> new ListBookDto(book));
    }

    public Page<ListBookDto> findPaginated(@Min(1) int pageNo, int pageSize) {
        return repository.findBooksByDeletedOn(null, PageRequest.of(pageNo, pageSize))
                .map(book -> new ListBookDto(book));
    }

    public Page<ListBookDto> findBehindPaginated(@Min(1) int pageNo, int pageSize) {
        return borrowingRepository.findAllByShouldReturnOnBeforeAndReturnedOnIsNull(PageRequest.of(pageNo, pageSize),
                LocalDate.now()).map(borrowing -> new ListBookDto(borrowing));
    }

}

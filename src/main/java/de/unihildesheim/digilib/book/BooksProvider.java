package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.book.model.BookDto;
import de.unihildesheim.digilib.book.model.ListBookDto;
import de.unihildesheim.digilib.borrowing.BorrowingRepository;
import de.unihildesheim.digilib.genre.Genre;
import de.unihildesheim.digilib.genre.GenreProvider;
import de.unihildesheim.digilib.utils.ISBNUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

@Service
public class BooksProvider {

    public static final int BOOKS_BEHIND_DAYS = 1;
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

    public void importCSV(InputStream input) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, "Cp1252"))) {
            String line;
            while ((line = br.readLine()) != null) {
                importBook(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importBook(String input) {
        String[] parts = input.split(Pattern.quote("|"));
        BookDto dto = new BookDto();
        dto.setAuthor(parts[0].isBlank() ? "Marvin Game" : parts[0]);
        dto.setTitle(parts[1].isBlank() ? "How To Act Like You Care" : parts[1]);
        //dto.setInvnr(Long.toString(System.currentTimeMillis()).substring(6));
        dto.setInvnr(parts[2].isBlank() ? Long.toString(System.currentTimeMillis()).substring(6) : parts[2].replace("/", "-"));
        //deren Invnrs haben forward slashs
        try {
            dto.setGenre(parts[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            dto.setGenre("Thriller");
        }
        create(dto);
    }
}

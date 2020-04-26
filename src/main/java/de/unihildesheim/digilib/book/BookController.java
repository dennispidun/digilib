package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.borrow.BorrowingService;
import de.unihildesheim.digilib.book.borrow.BorrowingsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    final BookRepository repository;

    final BorrowingService borrowingService;

    final BooksProvider booksProvider;

    public BookController(BookRepository repository, BorrowingService borrowingService, BooksProvider booksProvider) {
        this.repository = repository;
        this.borrowingService = borrowingService;
        this.booksProvider = booksProvider;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ListBookDto> getBooks(@RequestParam(required = false) String search) {
        if (search == null || search.isEmpty()) {
            return repository.findAll().stream().map(mapBookAndAddBorrowing()).collect(Collectors.toList());
        } else {
            List<Book> invnrBooks = repository.findBooksByInvnrContaining(search);
            List<Book> isbnBooks = repository.findBooksByIsbnContaining(search);

            List<Book> foundBooks = new ArrayList<>();

            if (invnrBooks.size() > 0) {
                foundBooks.addAll(invnrBooks);
            }

            if (isbnBooks.size() > 0) {
                foundBooks.addAll(isbnBooks);
            }

            return foundBooks.stream().distinct().map(mapBookAndAddBorrowing()).collect(Collectors.toList());
        }
    }

    private Function<Book, ListBookDto> mapBookAndAddBorrowing() {
        return book -> {
            ListBookDto bookDto = mapBook(book);
            addBorrowHistory(book, bookDto);
            return bookDto;
        };
    }

    private void addBorrowHistory(Book book, ListBookDto bookDto) {
        try {
            BorrowingsDto latestBorrowing = borrowingService.getLatestBorrowing(book.getInvnr());
            if (latestBorrowing.getReturnedOn() == null && latestBorrowing.getBorrowedOn() != null) {
                bookDto.setBorrowedOn(latestBorrowing.getBorrowedOn());
                bookDto.setBorrowerName(latestBorrowing.getBorrower().getFirstname() + " " + latestBorrowing.getBorrower().getLastname());
            }
        } catch (Exception e) {
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookDto createBook) {

        return ResponseEntity.ok(booksProvider.create(createBook));
    }

    @RequestMapping(value = "/{invnr}", method = RequestMethod.GET)
    public ResponseEntity<ListBookDto> getBook(@PathVariable("invnr") String invnr) {
        Book book = repository.findBookByInvnr(invnr).orElseThrow(() -> new BookNotFoundException(invnr));
        ListBookDto bookDto = mapBook(book);

        addBorrowHistory(book, bookDto);
        return ResponseEntity.ok().body(bookDto);
    }

    private ListBookDto mapBook(Book book) {
        ListBookDto bookDto = new ListBookDto();
        bookDto.setIsbn(book.getIsbn());
        bookDto.setInvnr(book.getInvnr());
        bookDto.setTitle(book.getTitle());
        bookDto.setCreatedOn(book.getCreatedOn());
        bookDto.setAuthor(book.getAuthor());
        return bookDto;
    }


}

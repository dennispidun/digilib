package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.borrow.BorrowingService;
import de.unihildesheim.digilib.book.borrow.BorrowingsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    final BookRepository repository;

    final BorrowingService borrowingService;

    public BookController(BookRepository repository, BorrowingService borrowingService) {
        this.repository = repository;
        this.borrowingService = borrowingService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Book> getBooks() {
        return repository.findAll();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Book> createBook(@Valid @RequestBody CreateBookDto createBook) {
        Book book = new Book();
        book.setAuthor(createBook.getAuthor());
        book.setTitle(createBook.getTitle());
        book.setInvnr(createBook.getInvnr());
        book.setIsbn(createBook.getIsbn());

        book.setCreatedOn(createBook.getCreatedOn());
        if (createBook.getCreatedOn() == null) {
            book.setCreatedOn(new Date());
        }

        return ResponseEntity.ok(repository.save(book));
    }

    @RequestMapping(value = "/{invnr}", method = RequestMethod.GET)
    public ResponseEntity<ListBookDto> getBook(@PathVariable("invnr") String invnr) {
        Book book = repository.findBookByInvnr(invnr).orElseThrow(() -> new BookNotFoundException());
        ListBookDto bookDto = new ListBookDto();
        bookDto.setIsbn(book.getIsbn());
        bookDto.setInvnr(book.getInvnr());
        bookDto.setTitle(book.getTitle());
        bookDto.setCreatedOn(book.getCreatedOn());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setCreatedOn(book.getCreatedOn());

        try {
            BorrowingsDto latestBorrowing = borrowingService.getLatestBorrowing(invnr);
            bookDto.setBorrowedOn(latestBorrowing.getBorrowedOn());
            bookDto.setReturnedOn(latestBorrowing.getReturnedOn());
        } catch (Exception e) { }

        return ResponseEntity.ok().body(bookDto);
    }


}

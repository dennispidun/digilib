package de.unihildesheim.digilib.book;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
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

        book.setBorrowedOn(createBook.getBorrowedOn());
        book.setCreatedOn(createBook.getCreatedOn());
        if (createBook.getCreatedOn() == null) {
            book.setCreatedOn(new Date());
        }

        return ResponseEntity.ok(repository.save(book));
    }

    @RequestMapping(value = "/{invnr}", method = RequestMethod.GET)
    public ResponseEntity<Book> getBook(@PathVariable("invnr") String invnr) {
        return ResponseEntity.ok(repository.findBookByInvnr(invnr).orElseThrow(() -> new BookNotFoundException()));
    }


}

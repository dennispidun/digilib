package de.unihildesheim.digilib.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Book> createBook(@RequestBody CreateBookDto createBook) {
        Book book = new Book();
        book.setAuthor(createBook.getAuthor());
        book.setBorrowedOn(createBook.getBorrowedOn());
        book.setCreatedOn(createBook.getCreatedOn());
        book.setTitle(createBook.getTitle());
        book.setInvnr(createBook.getInvnr());
        book.setIsbn(createBook.getIsbn());

        return ResponseEntity.ok(repository.save(book));
    }



}

package de.unihildesheim.digilib.book;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BooksProvider {

    private BookRepository repository;

    public BooksProvider(BookRepository repository) {
        this.repository = repository;
    }

    public Book create(BookDto createBook) {
        ISBNUtils.validateIsbn13(createBook.getIsbn());


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

}

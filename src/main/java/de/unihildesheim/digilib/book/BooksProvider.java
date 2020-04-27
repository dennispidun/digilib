package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.book.model.BookDto;
import de.unihildesheim.digilib.utils.ISBNUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Book> searchFor(String search) {
        List<Book> invnrBooks = repository.findBooksByInvnrContaining(search);
        List<Book> isbnBooks = repository.findBooksByIsbnContaining(search);

        List<Book> foundBooks = new ArrayList<>();

        if (invnrBooks.size() > 0) {
            foundBooks.addAll(invnrBooks);
        }

        if (isbnBooks.size() > 0) {
            foundBooks.addAll(isbnBooks);
        }

        return foundBooks.stream().distinct().collect(Collectors.toList());
    }

}

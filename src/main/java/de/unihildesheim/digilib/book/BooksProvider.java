package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.book.model.BookDto;
import de.unihildesheim.digilib.book.model.ListBookDto;
import de.unihildesheim.digilib.utils.ISBNUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<ListBookDto> searchForPaginated(String search, int pageNo, int pageSize) {

        Pageable paging = PageRequest.of(pageNo, pageSize);

        List<Book> invnrBooks = repository.findBooksByInvnrContaining(search, paging).toList();
        List<Book> isbnBooks = repository.findBooksByIsbnContaining(search, paging).toList();

        List<Book> foundBooks = new ArrayList<>();

        if (invnrBooks.size() > 0) {
            foundBooks.addAll(invnrBooks);
        }

        if (isbnBooks.size() > 0) {
            foundBooks.addAll(isbnBooks);
        }

        return foundBooks.stream()
                .distinct()
                .map(book -> new ListBookDto(book))
                .collect(Collectors.toList());
    }

    public List<ListBookDto> findPaginated(int pageNo, int pageSize) {
        return repository.findAll(PageRequest.of(pageNo, pageSize)).toList()
                .stream()
                .distinct()
                .map(book -> new ListBookDto(book))
                .collect(Collectors.toList());
    }

}

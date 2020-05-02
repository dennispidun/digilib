package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

    List<Book> findAll();

    Optional<Book> findBookByInvnr(String invnr);

    Page<Book> findBooksByInvnrContainingOrIsbnContaining(String invnr, String isbn, Pageable pageable);

    Page<Book> findAll(Pageable pageable);

}

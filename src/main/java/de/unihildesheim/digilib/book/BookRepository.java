package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByInvnr(String invnr);

    List<Book> findBooksByInvnrContaining(String invnr);

    List<Book> findBooksByIsbnContaining(String isbn);

}

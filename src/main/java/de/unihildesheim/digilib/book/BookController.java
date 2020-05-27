package de.unihildesheim.digilib.book;

import com.fasterxml.jackson.databind.JsonNode;
import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.book.model.BookDto;
import de.unihildesheim.digilib.book.model.BookModelMapper;
import de.unihildesheim.digilib.book.model.ListBookDto;
import de.unihildesheim.digilib.borrowing.BorrowingService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    final BookRepository repository;

    final BorrowingService borrowingService;

    final BooksProvider booksProvider;

    final BookModelMapper bookModelMapper;

    public BookController(BookRepository repository, BorrowingService borrowingService, BooksProvider booksProvider, BookModelMapper bookModelMapper) {
        this.repository = repository;
        this.borrowingService = borrowingService;
        this.booksProvider = booksProvider;
        this.bookModelMapper = bookModelMapper;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<ListBookDto> getBooks(@RequestParam(required = false) String search,
                                      @RequestParam int pageNo,
                                      @RequestParam int pageSize,
                                      @RequestParam(required = false) Boolean behind) {
        behind = behind != null ? behind : false;

        if (!behind) {
            if (search == null || search.isEmpty()) {
                return booksProvider.findPaginated(pageNo, pageSize);
            } else {
                return booksProvider.searchForPaginated(search, pageNo, pageSize);
            }
        } else {
            return booksProvider.findBehindPaginated(pageNo, pageSize);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookDto createBook) {
        return ResponseEntity.ok(booksProvider.create(createBook));
    }

    @RequestMapping(value = "/{invnr}", method = RequestMethod.GET)
    public ResponseEntity<ListBookDto> getBook(@PathVariable("invnr") String invnr) {
        Book book = repository.findBookByInvnr(invnr).orElseThrow(() -> new BookNotFoundException(invnr));
        ListBookDto bookDto = bookModelMapper.mapToListBook(book);
        return ResponseEntity.ok().body(borrowingService.addBorrowHistory(bookDto));
    }

    @PostMapping("/import")
    public ResponseEntity importBooks(@RequestParam("file") MultipartFile file) {
        try {
            this.booksProvider.importCSV(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

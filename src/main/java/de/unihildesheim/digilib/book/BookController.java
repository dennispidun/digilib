package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.model.*;
import de.unihildesheim.digilib.borrowing.BorrowingService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    final BookRepository repository;

    final BorrowingService borrowingService;

    final BooksProvider booksProvider;

    final BookModelMapper bookModelMapper;

    final ImportHandler importHandler;

    public BookController(BookRepository repository, BorrowingService borrowingService, BooksProvider booksProvider, BookModelMapper bookModelMapper, ImportHandler importHandler) {
        this.repository = repository;
        this.borrowingService = borrowingService;
        this.booksProvider = booksProvider;
        this.bookModelMapper = bookModelMapper;
        this.importHandler = importHandler;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<ListBookDto> getBooks(@RequestParam(required = false) String search,
                                      @RequestParam int pageNo,
                                      @RequestParam int pageSize,
                                      @RequestParam(required = false) Boolean behind) {
        if (pageNo < 0) {
            throw new PageNoBelowZeroException(pageNo);
        }

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
        String decInvnr = URLDecoder.decode(invnr, StandardCharsets.UTF_8);
        Book book = repository.findBookByInvnr(decInvnr).orElseThrow(() -> new BookNotFoundException(decInvnr));
        ListBookDto bookDto = bookModelMapper.mapToListBook(book);
        return ResponseEntity.ok().body(borrowingService.addBorrowHistory(bookDto));
    }

    @PostMapping("/import")
    public ResponseEntity importBooks(@RequestParam("file") Optional<MultipartFile> file,
                                      @RequestParam("delimiter") char d, @RequestParam("pos") String pos,
                                      @RequestParam("path") Optional<String> path) {
        this.importHandler.setPos(pos);
        try {
            if (file.isPresent()) {
                this.importHandler.importCSV(file.get().getInputStream(), d);
            } else {
                this.importHandler.importCSV(new FileInputStream(new File("." + path.get() + "testcsv.csv")), d);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

}

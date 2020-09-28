package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.book.imports.ImportHandler;
import de.unihildesheim.digilib.book.imports.ImportResultDto;
import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.book.model.BookDto;
import de.unihildesheim.digilib.book.model.ListBookDto;
import de.unihildesheim.digilib.book.model.UpdateBookDto;
import de.unihildesheim.digilib.borrowing.BorrowingService;
import de.unihildesheim.digilib.genre.Genre;
import de.unihildesheim.digilib.genre.GenreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    final BookRepository repository;
    final GenreRepository genreRepository;

    final BorrowingService borrowingService;

    final BooksProvider booksProvider;

    final ImportHandler importHandler;

    public BookController(BookRepository repository, GenreRepository genreRepository, BorrowingService borrowingService, BooksProvider booksProvider, ImportHandler importHandler) {
        this.repository = repository;
        this.genreRepository = genreRepository;
        this.borrowingService = borrowingService;
        this.booksProvider = booksProvider;
        this.importHandler = importHandler;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<ListBookDto> getBooks(@RequestParam(required = false) String search,
                                      @RequestParam int pageNo,
                                      @RequestParam int pageSize,
                                      @RequestParam(required = false) Integer behind) {
        if (pageNo < 0) {
            throw new PageNoBelowZeroException(pageNo);
        }

        if (behind == null) {
            if (search == null || search.isEmpty()) {
                return booksProvider.findPaginated(pageNo, pageSize);
            } else {
                return booksProvider.searchForPaginated(search, pageNo, pageSize);
            }
        } else {
            return booksProvider.findBooksBehindPaginated(pageNo, pageSize, behind);
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
        ListBookDto bookDto = new ListBookDto(book);
        return ResponseEntity.ok().body(borrowingService.addBorrowHistory(bookDto));
    }

    @GetMapping("/archived")
    public Page<ListBookDto> getArchived(@RequestParam int pageNo,
                                         @RequestParam int pageSize) {
        if (pageNo < 0) {
            throw new PageNoBelowZeroException(pageNo);
        }
        return repository.findByDeletedOnIsNotNull(PageRequest.of(pageNo, pageSize)).map(book -> new ListBookDto(book));
    }

    @PatchMapping("/{invnr}")
    public ResponseEntity<Book> modifyBook(@PathVariable("invnr") String invnr, @Valid @RequestBody UpdateBookDto dto) {
        Book book = repository.findBookByInvnr(invnr).orElseThrow(() -> new BookNotFoundException(invnr));
        book.setPrice(dto.getPrice());
        book.setComment(dto.getComment());
        book.setType(dto.getType());
        book.setAuthor(dto.getAuthor());
        book.setDeletedOn(dto.getDeletedOn());

        if (book.getGenre() == null ||
                (dto.getGenre() != null && !book.getGenre().getGenre().equalsIgnoreCase(dto.getGenre().getGenre()))) {
            Genre newGenre = genreRepository.findByGenre(dto.getGenre().getGenre())
                    .orElse(new Genre(dto.getGenre().getGenre()));
            genreRepository.save(newGenre);

            book.setGenre(newGenre);
        }
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        return ResponseEntity.ok(repository.save(book));
    }

    @PostMapping("/import")
    public ResponseEntity<ImportResultDto> importBooks(@RequestParam("file") Optional<MultipartFile> file,
                                                       @RequestParam("delimiter") char d,
                                                       @RequestParam("utf8") boolean en,
                                                       @RequestParam("pos") String pos,
                                                       @RequestParam("path") Optional<String> path) {
        try {
            if (file.isPresent()) {
                return this.importHandler.importCSV(file.get().getInputStream(), d, en, pos).report();
            } else if (path.isPresent()){
                return this.importHandler.importLocal(path.get(), d, en, pos).report();
            } else {
                return new ImportResultDto("Es wurde nichts zum Importieren übergeben.").report();
            }
        } catch (IOException e) {
            return new ImportResultDto(e.getMessage()).report();
        }
    }

}

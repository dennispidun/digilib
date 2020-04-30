package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.borrowing.model.BorrowingDto;
import de.unihildesheim.digilib.borrowing.model.CreateBorrowingDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books/{invnr}")
class BorrowingController {

    public static final int MAX_HISTORY_ENTRIES = 8;

    private BorrowingService service;

    public BorrowingController(BorrowingService service) {
        this.service = service;
    }

    @RequestMapping(value = "/borrowings", method = RequestMethod.POST)
    private ResponseEntity addBorrowing(@PathVariable("invnr") String invnr, @RequestBody CreateBorrowingDto createBorrowingDto) throws BookAlreadyBorrowedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.borrow(createBorrowingDto, invnr));
    }

    @RequestMapping(value = "/borrowings", method = RequestMethod.GET)
    private ResponseEntity<List<BorrowingDto>> getBorrowings(@PathVariable("invnr") String invnr) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getBorrowings(invnr)
                .stream()
                .limit(MAX_HISTORY_ENTRIES)
                .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/latest-borrowing", method = RequestMethod.GET)
    private ResponseEntity<BorrowingDto> latestBorrowing(@PathVariable("invnr") String invnr) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getLatestBorrowing(invnr));
    }

    @RequestMapping(value = "/latest-borrowing", method = RequestMethod.DELETE)
    private ResponseEntity<BorrowingDto> cancelLatestBorrowing(@PathVariable("invnr") String invnr) {
        return ResponseEntity.status(HttpStatus.OK).body(service.cancelLatestBorrowing(invnr));
    }

}

package de.unihildesheim.digilib.book.borrow;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/{invnr}")
public class BorrowingController {

    private BorrowingService service;

    public BorrowingController(BorrowingService service) {
        this.service = service;
    }

    @RequestMapping(value = "/borrowings", method = RequestMethod.POST)
    private ResponseEntity addBorrowing(@PathVariable("invnr") String invnr, @RequestBody CreateBorrowingDto createBorrowingDto) {
        service.borrow(createBorrowingDto, invnr);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/borrowings", method = RequestMethod.GET)
    private ResponseEntity<List<BorrowingsDto>> getBorrowings(@PathVariable("invnr") String invnr) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getBorrowings(invnr));
    }

    @RequestMapping(value = "/latest-borrowing", method = RequestMethod.GET)
    private ResponseEntity<BorrowingsDto> latestBorrowing(@PathVariable("invnr") String invnr) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getLatestBorrowing(invnr));
    }




}

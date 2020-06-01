package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.borrowing.model.BorrowingDto;
import de.unihildesheim.digilib.borrowing.model.CreateBorrowingDto;
import de.unihildesheim.digilib.user.User;
import de.unihildesheim.digilib.user.UserNotFoundException;
import de.unihildesheim.digilib.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books/{invnr}")
class BorrowingController {

    public static final int MAX_HISTORY_ENTRIES = 8;

    private BorrowingService service;

    private UserRepository userRepository;

    public BorrowingController(BorrowingService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/borrowings", method = RequestMethod.POST)
    private ResponseEntity addBorrowing(Principal user, @PathVariable("invnr") String invnr, @RequestBody CreateBorrowingDto createBorrowingDto) throws BookAlreadyBorrowedException {
        User loggedInUser = this.userRepository.findUserByUsername(user.getName())
                .orElseThrow(() -> new UserNotFoundException(user.getName()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.borrow(createBorrowingDto, invnr, loggedInUser));
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

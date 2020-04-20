package de.unihildesheim.digilib.book.borrow;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Book already borrowed")
public class BookAlreadyBorrowedException extends RuntimeException {
}

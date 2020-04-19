package de.unihildesheim.digilib.book.borrow;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Book never borrowed")
public class BookNeverBorrowedException extends RuntimeException {
}

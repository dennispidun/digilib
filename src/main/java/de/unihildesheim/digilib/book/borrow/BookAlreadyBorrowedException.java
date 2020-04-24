package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BookAlreadyBorrowedException extends ApplicationRuntimeException {

    public BookAlreadyBorrowedException(String invnr) {
        super("Book with Inventory-Nr: " + invnr + " already borrowed", HttpStatus.BAD_REQUEST);
    }
}

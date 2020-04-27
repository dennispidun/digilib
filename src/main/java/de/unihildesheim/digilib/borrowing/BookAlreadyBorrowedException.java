package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class BookAlreadyBorrowedException extends ApplicationRuntimeException {

    public BookAlreadyBorrowedException(String invnr) {
        super("Book with Inventory-Nr: " + invnr + " already borrowed", HttpStatus.BAD_REQUEST);
    }
}

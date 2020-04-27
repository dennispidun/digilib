package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class BookNeverBorrowedException extends ApplicationRuntimeException {

    public BookNeverBorrowedException(String invnr) {
        super("Book with Inventory-Nr: " + invnr + " never borrowed", HttpStatus.NOT_FOUND);
    }
}

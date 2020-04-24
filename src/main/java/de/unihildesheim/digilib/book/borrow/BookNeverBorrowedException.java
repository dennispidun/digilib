package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BookNeverBorrowedException extends ApplicationRuntimeException {

    public BookNeverBorrowedException(String invnr) {
        super("Book with Inventory-Nr: " + invnr + " never borrowed", HttpStatus.NOT_FOUND);
    }
}

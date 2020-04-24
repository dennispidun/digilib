package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class BookNotFoundException extends ApplicationRuntimeException {

    public BookNotFoundException(String invnr) {
        super("Book with Inventory-Nr: " + invnr + " not found.", HttpStatus.NOT_FOUND);
    }
}

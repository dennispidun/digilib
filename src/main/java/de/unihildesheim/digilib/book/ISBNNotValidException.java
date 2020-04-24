package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ISBNNotValidException extends ApplicationRuntimeException {


    public ISBNNotValidException(String isbn) {
        super("Provided ISBN: " + isbn + " is not valid", HttpStatus.BAD_REQUEST);
    }
}

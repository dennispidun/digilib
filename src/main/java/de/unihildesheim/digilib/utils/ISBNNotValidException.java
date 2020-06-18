package de.unihildesheim.digilib.utils;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class ISBNNotValidException extends ApplicationRuntimeException {


    public ISBNNotValidException(String isbn) {
        super("Provided ISBN: " + isbn + " is not valid", HttpStatus.BAD_REQUEST);
    }
}

package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class BorrowerNotFoundException extends ApplicationRuntimeException {
    public BorrowerNotFoundException(long id) {
        super("Provided borrower: " + id + " not found", HttpStatus.NOT_FOUND);
    }
}

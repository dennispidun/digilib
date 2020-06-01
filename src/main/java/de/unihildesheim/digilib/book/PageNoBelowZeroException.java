package de.unihildesheim.digilib.book;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class PageNoBelowZeroException extends ApplicationRuntimeException {
    public PageNoBelowZeroException(int pageNo) {
        super("Page number is not acceptable: " + pageNo, HttpStatus.BAD_REQUEST);
    }
}

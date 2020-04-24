package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class StudentCannotBorrowMultipleBooks extends ApplicationRuntimeException{
    public StudentCannotBorrowMultipleBooks() {
        super("Student cannot borrow multiple books", HttpStatus.BAD_REQUEST);
    }
}

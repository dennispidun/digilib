package de.unihildesheim.digilib.borrowing;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

class StudentCannotBorrowMultipleBooks extends ApplicationRuntimeException{
    public StudentCannotBorrowMultipleBooks() {
        super("Student cannot borrow multiple books", HttpStatus.BAD_REQUEST);
    }
}

package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.student.Borrower;
import lombok.Data;

import java.util.Date;

@Data
public class BorrowingsDto {

    private Borrower borrower;
    private Date borrowedOn;
    private Date returnedOn;
}

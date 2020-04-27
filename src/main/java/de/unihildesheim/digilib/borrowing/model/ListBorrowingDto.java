package de.unihildesheim.digilib.borrowing.model;

import lombok.Data;

import java.util.Date;

@Data
public class ListBorrowingDto {

    private Borrower borrower;
    private Date borrowedOn;
    private Date returnedOn;
}

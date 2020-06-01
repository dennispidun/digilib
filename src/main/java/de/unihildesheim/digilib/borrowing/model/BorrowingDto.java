package de.unihildesheim.digilib.borrowing.model;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowingDto {

    private Borrower borrower;

    private String lenderFirstname;
    private String lenderLastname;
    private Date borrowedOn;
    private Date returnedOn;
}

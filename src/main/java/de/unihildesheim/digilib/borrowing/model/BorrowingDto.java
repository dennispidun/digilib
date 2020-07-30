package de.unihildesheim.digilib.borrowing.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BorrowingDto {

    private Borrower borrower;

    private String lenderFirstname;
    private String lenderLastname;
    private LocalDateTime borrowedOn;
    private LocalDateTime returnedOn;
    private LocalDate shouldReturnOn;
    private Integer daysOverdue;
}

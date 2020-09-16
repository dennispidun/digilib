package de.unihildesheim.digilib.borrowing.model;

import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.user.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime borrowedOn;

    private LocalDateTime returnedOn;

    @Column(nullable = false)
    private LocalDate shouldReturnOn;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Borrower borrower;

    @ManyToOne
    private User lender;



}

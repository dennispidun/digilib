package de.unihildesheim.digilib.borrowing.model;

import de.unihildesheim.digilib.book.model.Book;
import de.unihildesheim.digilib.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Date borrowedOn;

    private Date returnedOn;

    @Column(nullable = false)
    private Date shouldReturnOn;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Borrower borrower;

    @ManyToOne
    private User lender;



}

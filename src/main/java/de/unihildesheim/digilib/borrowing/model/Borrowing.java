package de.unihildesheim.digilib.borrowing.model;

import de.unihildesheim.digilib.book.model.Book;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date borrowedOn;
    private Date returnedOn;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Borrower borrower;

}

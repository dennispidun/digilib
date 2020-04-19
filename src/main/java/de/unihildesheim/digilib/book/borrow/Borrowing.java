package de.unihildesheim.digilib.book.borrow;

import de.unihildesheim.digilib.book.Book;
import de.unihildesheim.digilib.student.Student;
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
    private Student student;

}

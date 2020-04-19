package de.unihildesheim.digilib.book;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String invnr;
    private String isbn;
    private String title;
    private String author;
    private Date createdOn;
}

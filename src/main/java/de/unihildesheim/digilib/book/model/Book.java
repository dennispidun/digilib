package de.unihildesheim.digilib.book.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.unihildesheim.digilib.borrowing.model.Borrowing;
import de.unihildesheim.digilib.genre.Genre;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String invnr;

    @ISBN
    private String isbn;
    private String title;
    private String author;
    private Date createdOn;

    @OneToMany(targetEntity = Borrowing.class, mappedBy = "book", fetch = FetchType.LAZY)
    @OrderBy("borrowedOn DESC")
    @JsonIgnore
    public List<Borrowing> borrowings;

    @ManyToOne
    public Genre genre;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

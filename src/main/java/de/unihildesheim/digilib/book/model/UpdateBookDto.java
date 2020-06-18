package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.genre.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookDto {

    @NotEmpty
    private String invnr;

    @ISBN
    private String isbn;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    private Genre genre;

    private Date createdOn;

    private String type;
    private String comment;
    private String price;
    private Date deletedOn;
}

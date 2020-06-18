package de.unihildesheim.digilib.book.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @NotEmpty
    @UniqueInventoryNumber
    private String invnr;

    @NotEmpty
    @ISBN
    private String isbn;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    private String genre;

    private Date createdOn;

    private String type;
    private String comment;
}

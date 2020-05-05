package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.book.ISBN;
import de.unihildesheim.digilib.book.UniqueInventoryNumber;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
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

    private Date createdOn;
}

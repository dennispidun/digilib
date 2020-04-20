package de.unihildesheim.digilib.book;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class BookDto {

    @NotEmpty
    private String invnr;

    @NotEmpty
    private String isbn;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    private Date createdOn;
}

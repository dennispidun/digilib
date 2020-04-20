package de.unihildesheim.digilib.book;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Value
public class CreateBookDto {

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
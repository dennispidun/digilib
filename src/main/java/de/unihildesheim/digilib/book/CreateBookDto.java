package de.unihildesheim.digilib.book;

import lombok.Value;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

@Value
public class CreateBookDto {

    private String invnr;
    private String isbn;
    private String title;
    private String author;
    private Date borrowedOn;
    private Date createdOn;

}

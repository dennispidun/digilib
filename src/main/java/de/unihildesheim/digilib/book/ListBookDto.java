package de.unihildesheim.digilib.book;

import lombok.Data;

import java.util.Date;

@Data
public class ListBookDto {

    private String invnr;
    private String isbn;
    private String title;
    private String author;
    private Date createdOn;
    private Date borrowedOn;
    private Date returnedOn;

}


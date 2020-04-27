package de.unihildesheim.digilib.book.model;

import lombok.Data;

import java.util.Date;

@Data
public class ListBookDto extends BookDto {

    private Date borrowedOn;
    private String borrowerName;
    private Date returnedOn;

}


package de.unihildesheim.digilib.book.imports;

import de.unihildesheim.digilib.book.model.BookDto;

import java.util.List;

public class ImportResultDto {

    private int successfull;

    private int failed;

    private List<BookDto> alreadyExist;

    private String delimiterErrors;

    private List<BookDto> failedBooks;
}

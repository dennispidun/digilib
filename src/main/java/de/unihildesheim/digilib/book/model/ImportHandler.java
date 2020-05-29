package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.book.BooksProvider;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

@Component
public class ImportHandler {

    final BooksProvider booksProvider;

    String delimiter;

    public ImportHandler(BooksProvider booksProvider) {
        this.booksProvider = booksProvider;
    }

    public void importCSV(InputStream input, char d) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, "Cp1252"))) {
            String line;
            delimiter = String.valueOf(d);
            while ((line = br.readLine()) != null) {
                importBook(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importBook(String input) {
        String[] parts = splitString(input);
        BookDto dto = new BookDto();
        dto.setAuthor(parts[0].equals(delimiter) ? "Musterautor" : parts[0].substring(0, parts[0].length() - 1));
        dto.setTitle(parts[1].equals(delimiter) ? "Mustertitel" : parts[1].substring(0, parts[1].length() - 1));
        //dto.setInvnr(Long.toString(System.currentTimeMillis()).substring(6));
        dto.setInvnr(parts[2].isBlank() ? Long.toString(System.currentTimeMillis()).substring(6) : parts[2].substring(0, parts[2].length() - 1).replace("/", "-"));
        //deren Invnrs haben forward slashs
        try {
            dto.setGenre(parts[3].substring(0, parts[3].length() - 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            dto.setGenre("Thriller");
        }
        booksProvider.create(dto);
    }

    private String[] splitString(String input) {
        if (input.contains(delimiter)) {
            return input.split("(?<=" + Pattern.quote(delimiter) + ")");
        } else {
            return null;
        }
    }

}

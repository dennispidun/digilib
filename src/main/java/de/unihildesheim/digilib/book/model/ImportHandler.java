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

    public ImportHandler(BooksProvider booksProvider) {
        this.booksProvider = booksProvider;
    }

    public void importCSV(InputStream input) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, "Cp1252"))) {
            String line;
            while ((line = br.readLine()) != null) {
                importBook(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importBook(String input) {
        String[] parts = splitString(input);
        System.out.println(parts.length);
        BookDto dto = new BookDto();
        dto.setAuthor(parts[0].equals("|") ? "Musterautor" : parts[0].substring(0, parts[0].length() - 1));
        dto.setTitle(parts[1].equals("|") ? "Mustertitel" : parts[1].substring(0, parts[1].length() - 1));
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
        if (input.contains("|")) {
            return input.split("(?<=" + Pattern.quote("|") + ")");
        } else {
            return null;
        }
    }

}

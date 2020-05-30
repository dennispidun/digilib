package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.book.BooksProvider;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.regex.Pattern;

@Component
public class ImportHandler {

    final BooksProvider booksProvider;

    String delimiter;

    int[] p = new int[5];

    public ImportHandler(BooksProvider booksProvider) {
        this.booksProvider = booksProvider;
    }

    public void importCSV(InputStream input, char d) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, "Cp1252"))) {
            String line;
            delimiter = String.valueOf(d);
            while ((line = br.readLine()) != null) {
                booksProvider.create(importBook(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPos(String pos) {
        for (int i = 0; i < 5; i++) {
            p[i] = pos.indexOf(String.valueOf(i));
        }
    }

    private BookDto importBook(String input) {
        //System.out.println(new File("./importfolder").getAbsolutePath());
        String[] parts = splitString(input);
        BookDto dto = new BookDto();
        if (parts != null) {
            dto.setAuthor(parts[p[0]].equals(delimiter) ? "Musterautor" : parts[p[0]].substring(0, parts[p[0]].length() - 1));        dto.setTitle(parts[p[1]].equals(delimiter) ? "Mustertitel" : parts[p[1]].substring(0, parts[p[1]].length() - 1));
            dto.setInvnr(parts[p[2]].isBlank() ? Long.toString(System.currentTimeMillis()).substring(6) : parts[p[2]].substring(0, parts[p[2]].length() - 1).replace("/", "-"));
            try {
                dto.setGenre(parts[p[3]].substring(0, parts[p[3]].length() - 1));
            } catch (ArrayIndexOutOfBoundsException e) {
                dto.setGenre("Musterbuchart");
            }
        }
        return dto;
    }

    private String[] splitString(String input) {
        if (input.contains(delimiter)) {
            return input.split("(?<=" + Pattern.quote(delimiter) + ")");
        } else {
            return null;
        }
    }

}

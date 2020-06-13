package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.book.BooksProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ImportHandler {

    final BooksProvider booksProvider;

    String delimiter;

    int[] p = {0, 1, 2, 3, 4};

    public ImportHandler(BooksProvider booksProvider) {
        this.booksProvider = booksProvider;
    }

    @PostConstruct
    public void checkLocal() {
        try {
            importLocal("/importfolder", '|');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importLocal(String path, char d) throws IOException {
        for (File fileP : Files.walk(Paths.get("."+ path)).filter(p -> p.toString().endsWith(".csv") &&
                Files.isRegularFile(p) && Files.isReadable(p)).map(Path::toFile).collect(Collectors.toList())) {
            importCSV(new FileInputStream(fileP), d);
        }
    }

    public void importCSV(InputStream input, char d) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input, "Cp1252"));
        String line;
        delimiter = String.valueOf(d);
        while ((line = br.readLine()) != null) {
            booksProvider.create(importBook(line));
        }
    }

    public void setPos(String pos) {
        for (int i = 0; i < 5; i++) {
            p[i] = pos.indexOf(String.valueOf(i));
        }
    }

    private BookDto importBook(String input) {
        String[] parts = splitString(input);
        BookDto dto = new BookDto();
        if (parts != null) {
            dto.setAuthor(parts[p[0]].equals(delimiter) ? "Musterautor" : parts[p[0]].substring(0, parts[p[0]].length() - 1));
            dto.setTitle(parts[p[1]].equals(delimiter) ? "Mustertitel" : parts[p[1]].substring(0, parts[p[1]].length() - 1));
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

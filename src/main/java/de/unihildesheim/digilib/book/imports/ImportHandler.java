package de.unihildesheim.digilib.book.imports;

import de.unihildesheim.digilib.book.BookRepository;
import de.unihildesheim.digilib.book.BooksProvider;
import de.unihildesheim.digilib.book.model.BookDto;
import org.aspectj.weaver.ast.Not;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ImportHandler {

    final BooksProvider booksProvider;
    private BookRepository repository;

    String delimiter;

    int[] p;

    public ImportHandler(BooksProvider booksProvider) {
        this.booksProvider = booksProvider;
    }

    @PostConstruct
    public void checkLocal() {
        try {
            p = new int[]{0, 1, 2, 3, 4};
            importLocal("/importfolder", '|');
        } catch (IOException | DataIntegrityViolationException | DelimiterNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void importLocal(String path, char d) throws IOException, DataIntegrityViolationException, DelimiterNotFoundException {
        for (File fileP : Files.walk(Paths.get("."+ path)).filter(p -> p.toString().endsWith(".csv") &&
                Files.isRegularFile(p) && Files.isReadable(p)).map(Path::toFile).collect(Collectors.toList())) {
            importCSV(new FileInputStream(fileP), d);
        }
    }

    public void importCSV(InputStream input, char d) throws IOException, DataIntegrityViolationException,
            DelimiterNotFoundException, NotEnoughInformationException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input, "Cp1252"));
        String line;
        delimiter = String.valueOf(d);
        while ((line = br.readLine()) != null) {
            booksProvider.create(importBook(line));
        }
    }

    public void setPos(String pos) {
        p = new int[5];
        for (int i = 0; i < 5; i++) {
            p[i] = pos.indexOf(String.valueOf(i));
        }
    }

    private BookDto importBook(String input) throws DelimiterNotFoundException{
        String[] parts = splitString(input);
        BookDto dto;
        try {
            dto = new BookDto();
            dto.setAuthor(parts[p[0]].isBlank() ? "Musterautor" : parts[p[0]]);
            dto.setTitle(parts[p[1]].isBlank() ? "Mustertitel" : parts[p[1]]);
            dto.setInvnr(parts[p[2]].isBlank() ? Long.toString(System.currentTimeMillis()).substring(6) : parts[p[2]].replace("/", "-"));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NotEnoughInformationException(input);
        }
        if (parts.length < 5) {
            String[] tmp = Arrays.copyOf(parts, 5);
            for (int i = parts.length; i < 5; i++) {
                tmp[i] = "";
            }
            parts = tmp;
        }
        dto.setGenre(parts[p[3]].isBlank() ? "Musterbuchart" : parts[p[3]]);
        //dto.setPrize(parts[p[4]].isBlank() ? "Musterpreis" : parts[p[4]]);
        return dto;
    }

    private String[] splitString(String input) {
        if (input.contains(delimiter)) {
            return input.split(Pattern.quote(delimiter), -1);
        } else {
            throw new DelimiterNotFoundException(delimiter, input);
        }
    }

}

package de.unihildesheim.digilib.book.imports;

import de.unihildesheim.digilib.book.BooksProvider;
import de.unihildesheim.digilib.book.model.BookDto;
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

    String delimiter;

    int[] p;

    public ImportHandler(BooksProvider booksProvider) {
        this.booksProvider = booksProvider;
    }

    @PostConstruct
    public void checkLocal() {
        p = new int[]{0, 1, 2, 3, 4};
        importLocal("/importfolder", '|');
    }

    public ImportResultDto importLocal(String path, char d) {
        ImportResultDto result = new ImportResultDto();
        if (new File(path).mkdirs()) {
            result.addErr(ImportError.FOLDEREMPTY, path);
        } else {
            try {
                for (File fileP : Files.walk(Paths.get("."+ path)).filter(p -> p.toString().endsWith(".csv") &&
                        Files.isRegularFile(p) && Files.isReadable(p)).map(Path::toFile).collect(Collectors.toList())) {
                    try {
                        result.addDto(importCSV(new FileInputStream(fileP), d));
                    } catch (FileNotFoundException e) {
                        result.addErr(ImportError.FILENOTFOUND, fileP);
                    }
                }
            } catch (IOException ex) {
                result.addErr(ImportError.IOEX, ex.getMessage());
            }
        }
        return result;
    }

    public ImportResultDto importCSV(InputStream input, char d) {
        ImportResultDto result = new ImportResultDto();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(input, "Cp1252"));
            String line;
            delimiter = String.valueOf(d);
            try {
                while ((line = br.readLine()) != null) {
                    if (line.length() > 4) {
                        try {
                            BookDto dto = importBook(line);
                            try {
                                booksProvider.create(dto);
                                result.incSuccess();
                            } catch (DataIntegrityViolationException de) {
                                result.addErr(ImportError.ALREADYEX, dto);
                                result.incFailed();
                            }
                        } catch (DelimiterNotFoundException dex) {
                            result.addErr(ImportError.DELIMITERERR, dex.getMessage());
                            result.incFailed();
                        } catch (NotEnoughInformationException ne) {
                            result.addErr(ImportError.NOTENOUGHINF, ne.getMessage());
                            result.incFailed();
                        }
                    } else {
                        result.incEmptyLines();
                    }
                }
            } catch (IOException ie) {
                result.addErr(ImportError.IOEX, ie.getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            result.addErr(ImportError.ENCODINGERR, e.getMessage());
        }
        return result;
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

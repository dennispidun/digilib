package de.unihildesheim.digilib.book.imports;

import de.unihildesheim.digilib.book.BooksProvider;
import de.unihildesheim.digilib.book.model.BookDto;
import de.unihildesheim.digilib.genre.Genre;
import de.unihildesheim.digilib.genre.GenreProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ImportHandler {

    final BooksProvider booksProvider;
    final GenreProvider genreProvider;

    public ImportHandler(BooksProvider booksProvider, GenreProvider genreProvider) {
        this.booksProvider = booksProvider;
        this.genreProvider = genreProvider;
    }

    @PostConstruct
    public void checkLocal() {
        importLocal("./importfolder", '|', "01234567");
    }

    public ImportResultDto importLocal(String path, char d, String pos) {
        ImportResultDto result = new ImportResultDto();
        if (new File(path).mkdirs()) {
            result.addErr(ImportError.FOLDEREMPTY, path);
        } else {
            try {
                for (File fileP : Files.walk(Paths.get(path)).filter(p -> p.toString().endsWith(".csv") &&
                        Files.isRegularFile(p) && Files.isReadable(p)).map(Path::toFile).collect(Collectors.toList())) {
                    try {
                        FileInputStream fs = new FileInputStream(fileP);
                        result.addDto(importCSV(fs, d, pos));
                        fs.close();
                        if (result.getRealErrs() == 0) {
                            File mv = new File("./imported");
                            mv.mkdirs();
                            Files.move(fileP.toPath(), Paths.get(mv.toPath() + "/" + "imported"
                                    + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now())
                                    + fileP.getName()));
                        }
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

    public ImportResultDto importCSV(InputStream input, char d, String p) {
        ImportResultDto result = new ImportResultDto();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(input, "Cp1252"));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    if (line.length() > 4) {
                        try {
                            BookDto dto = importBook(line, String.valueOf(d), setPos(p));
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

    public int[] setPos(String pos) {
        int[] p = new int[pos.length()];
        for (int i = 0; i < pos.length(); i++) {
            p[i] = pos.indexOf(String.valueOf(i));
        }
        return p;
    }

    private BookDto importBook(String input, String d, int[] p) throws DelimiterNotFoundException{
        String[] parts = splitString(input, d);
        BookDto dto;
        try {
            dto = new BookDto();
            dto.setAuthor(parts[p[0]].isBlank() ? "Musterautor" : parts[p[0]]);
            dto.setTitle(parts[p[1]].isBlank() ? "Mustertitel" : parts[p[1]]);
            dto.setInvnr(parts[p[2]].isBlank() ? Long.toString(System.currentTimeMillis()).substring(6) : parts[p[2]].replace("/", "-"));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NotEnoughInformationException(input);
        }
        if (parts.length < 8) {
            String[] tmp = Arrays.copyOf(parts, 8);
            for (int i = parts.length; i < 8; i++) {
                tmp[i] = "";
            }
            parts = tmp;
        }
        dto.setPrice(parts[p[4]].isBlank() ? "nicht bekannt" : parts[p[4]]);
        dto.setGenre(genreProvider.getOrSave(parts[p[5]].isBlank() ? new Genre("Mustergenre") : new Genre(parts[p[3]])));
        dto.setIsbn(parts[p[6]].isBlank() ? "-" : parts[p[6]]);
        dto.setComment(parts[p[7]].isBlank() ? "" : parts[p[7]]);
        dto.setDeletedOn(null);
        return dto;
    }

    private String[] splitString(String input, String d) {
        if (input.contains(d)) {
            return input.split(Pattern.quote(d), -1);
        } else {
            throw new DelimiterNotFoundException(d, input);
        }
    }

}

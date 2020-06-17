package de.unihildesheim.digilib.book.imports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
public class ImportResultDto {

    private int successfull = 0;
    private int failed = 0;
    private String alreadyExist = "";
    private String delimiterErrors = "";
    private String notEnoughInfo = "";
    private int emptyLines = 0;
    private String encodingErr = "";
    private String ioerr = "";
    private String fileNotFoundErr = "";
    private String folderEmpty = "";

    public void addAlreadyExists(String msg) { alreadyExist += msg + System.lineSeparator(); }
    public void incSuccess() { successfull++; }
    public void incFailed() { failed++; }
    public void incEmptyLines() { emptyLines++; }
    public void addDelErr(String msg) { delimiterErrors += msg + System.lineSeparator(); }
    public void addNotEnoughInfo(String msg) { notEnoughInfo += msg + System.lineSeparator(); }
    public void addEncodingErr(String msg) { encodingErr += msg + System.lineSeparator(); }
    public void addIoerr(String msg) { ioerr += msg + System.lineSeparator(); }
    public void addFileNotFound(String msg) { fileNotFoundErr += msg + System.lineSeparator(); }
    public void addFolderEmpty(String msg) { folderEmpty += msg + System.lineSeparator(); }

    public void addDto(ImportResultDto dto) {
        successfull += dto.getSuccessfull();
        failed += dto.getFailed();
        alreadyExist += dto.getAlreadyExist();
        delimiterErrors += dto.getDelimiterErrors();
        notEnoughInfo += dto.getNotEnoughInfo();
        emptyLines += dto.getEmptyLines();
        encodingErr += dto.getEncodingErr();
        ioerr += dto.getIoerr();
        fileNotFoundErr += dto.getFileNotFoundErr();
        folderEmpty += dto.getFolderEmpty();
    }

    public ResponseEntity report() {
        if (failed > 0 || !(encodingErr.equals("")) || !(ioerr.equals("")) || !(fileNotFoundErr.equals("")) || !(folderEmpty.equals(""))) {
            return ResponseEntity.badRequest().body(this);
        } else {
            return ResponseEntity.ok(this);
        }
    }
}

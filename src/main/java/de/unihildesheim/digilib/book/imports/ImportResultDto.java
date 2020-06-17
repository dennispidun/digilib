package de.unihildesheim.digilib.book.imports;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.EnumMap;

@Getter
public class ImportResultDto {

    private int successfull = 0;
    private int failed = 0;
    private int emptyLines = 0;

    EnumMap<ImportError, Object> errs;

    public void incSuccess() { successfull++; }
    public void incFailed() { failed++; }
    public void incEmptyLines() { emptyLines++; }

    public ImportResultDto() {
        errs = new EnumMap<>(ImportError.class);
    }

    public ImportResultDto(String ioex) {
        errs = new EnumMap<>(ImportError.class);
        errs.put(ImportError.IOEX, ioex);
    }

    public void addDto(ImportResultDto dto) {
        successfull += dto.getSuccessfull();
        failed += dto.getFailed();
        emptyLines += dto.getEmptyLines();
        errs.putAll(dto.getErrs());
    }

    public void addErr(ImportError err, Object o) {
        errs.put(err, o);
    }

    public ResponseEntity<ImportResultDto> report() {
        if (failed > 0 || errs.size() > 0) {
            return ResponseEntity.badRequest().body(this);
        } else {
            return ResponseEntity.ok(this);
        }
    }
}

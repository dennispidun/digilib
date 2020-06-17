package de.unihildesheim.digilib.book.imports;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Getter
public class ImportResultDto {

    private int successfull = 0;
    private int failed = 0;
    private int emptyLines = 0;

    EnumMap<ImportError, List<Object>> errs;

    public void incSuccess() { successfull++; }
    public void incFailed() { failed++; }
    public void incEmptyLines() { emptyLines++; }

    public int getRealErrs() {
        return errs.size() - (errs.containsKey(ImportError.ALREADYEX) ? 1 : 0);
    }

    public ImportResultDto() {
        errs = new EnumMap<>(ImportError.class);
    }

    public ImportResultDto(String ioex) {
        errs = new EnumMap<>(ImportError.class);
        addErr(ImportError.IOEX, ioex);
    }

    public void addDto(ImportResultDto dto) {
        successfull += dto.getSuccessfull();
        failed += dto.getFailed();
        emptyLines += dto.getEmptyLines();
        errs.putAll(dto.getErrs());
    }

    public void addErr(ImportError err, Object o) {
        if (!(errs.containsKey(err))) {
            errs.put(err, new ArrayList<>());
        }
        errs.get(err).add(o);
    }

    public ResponseEntity<ImportResultDto> report() {
        if (failed > 0 || getRealErrs() > 0) {
            return ResponseEntity.badRequest().body(this);
        } else {
            return ResponseEntity.ok(this);
        }
    }
}

package de.unihildesheim.digilib.apierror;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
    private String code;

    ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
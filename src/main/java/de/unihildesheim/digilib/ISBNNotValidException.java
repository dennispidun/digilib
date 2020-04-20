package de.unihildesheim.digilib;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Provided ISBN is not valid")
public class ISBNNotValidException extends RuntimeException {
}

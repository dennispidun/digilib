package de.unihildesheim.digilib.apierror;

import org.springframework.http.HttpStatus;

public abstract class ApplicationRuntimeException extends RuntimeException{

    private HttpStatus status;

    public ApplicationRuntimeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

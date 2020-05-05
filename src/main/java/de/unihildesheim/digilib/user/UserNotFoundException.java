package de.unihildesheim.digilib.user;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApplicationRuntimeException {
    public UserNotFoundException(String username) {
        super("User: " + username + " not found.", HttpStatus.NOT_FOUND);
    }
}

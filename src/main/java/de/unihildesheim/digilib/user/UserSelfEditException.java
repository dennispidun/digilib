package de.unihildesheim.digilib.user;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class UserSelfEditException extends ApplicationRuntimeException {
    public UserSelfEditException() {
        super("Du kannst nicht deinen eigenen Benutzer bearbeiten.", HttpStatus.NOT_FOUND);
    }
}

package de.unihildesheim.digilib.user;

import de.unihildesheim.digilib.apierror.ApplicationRuntimeException;
import org.springframework.http.HttpStatus;

public class UserSelfEditException extends ApplicationRuntimeException {
    public UserSelfEditException() {
        super("You cannot edit your own account", HttpStatus.NOT_FOUND);
    }
}

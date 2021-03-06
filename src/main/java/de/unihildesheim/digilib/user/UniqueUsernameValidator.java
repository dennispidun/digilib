package de.unihildesheim.digilib.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private UserRepository repository;

    public UniqueUsernameValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return repository.findUserByUsername(value).isEmpty();
    }
}

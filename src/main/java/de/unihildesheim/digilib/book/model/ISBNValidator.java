package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.utils.ISBNNotValidException;
import de.unihildesheim.digilib.utils.ISBNUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ISBNValidator implements ConstraintValidator<ISBN, String> {

    @Override
    public void initialize(ISBN constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean validISBN13 = true;
        boolean validISBN10 = true;

        try {
            ISBNUtils.validateIsbn13(value);
        } catch (ISBNNotValidException e) {
            validISBN13 = false;
        }

        try {
            ISBNUtils.validateIsbn10(value);
        } catch (ISBNNotValidException e) {
            validISBN10 = false;
        }

        return validISBN10 || validISBN13;
    }
}

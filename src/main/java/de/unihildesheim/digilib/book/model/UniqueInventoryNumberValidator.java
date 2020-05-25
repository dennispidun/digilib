package de.unihildesheim.digilib.book.model;

import de.unihildesheim.digilib.book.BookRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueInventoryNumberValidator implements ConstraintValidator<UniqueInventoryNumber, String> {

    private BookRepository repository;

    public UniqueInventoryNumberValidator(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public void initialize(UniqueInventoryNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return repository.findBookByInvnr(value).isEmpty();
    }
}

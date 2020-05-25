package de.unihildesheim.digilib.book.model;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueInventoryNumberValidator.class)
public @interface UniqueInventoryNumber {
    String message() default "Die angegebene Inventarnummer wird bereits verwendet";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

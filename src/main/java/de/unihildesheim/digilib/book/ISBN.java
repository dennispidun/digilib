package de.unihildesheim.digilib.book;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ISBNValidator.class)
public @interface ISBN {
    String message() default "Die angegebene ISBN entspricht nicht dem ISBN-10/13 Format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

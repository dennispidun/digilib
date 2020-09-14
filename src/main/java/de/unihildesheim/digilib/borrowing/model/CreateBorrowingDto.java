package de.unihildesheim.digilib.borrowing.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateBorrowingDto {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotNull(message = "Ausleihdauer darf nicht leer sein.")
    @Min(1)
    @Max(4)
    private Integer weeks;

}

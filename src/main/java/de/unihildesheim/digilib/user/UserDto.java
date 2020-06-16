package de.unihildesheim.digilib.user;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserDto {

    @NotEmpty
    @UniqueUsername
    @Min(value = 5, message = "Der Username muss mindestens 5 Zeichen haben.")
    @Max(value = 12, message = "Der Username darf maximal 12 Zeichen haben.")
    private String username;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    private String password;

    private boolean enabled;

    @NotNull
    private Role role;

}

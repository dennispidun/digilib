package de.unihildesheim.digilib.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    @NotEmpty
    @UniqueUsername
    @Size(min = 5, message = "Der Username muss mindestens 5 Zeichen haben.")
    @Size(max = 12, message = "Der Username darf maximal 12 Zeichen haben.")
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

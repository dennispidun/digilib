package de.unihildesheim.digilib.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @NotEmpty
    @UniqueUsername
    private String username;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String password;

    private boolean enabled;

    @NotNull
    private Role role;

}

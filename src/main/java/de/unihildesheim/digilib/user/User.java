package de.unihildesheim.digilib.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    @JsonIgnore
    private String password;

    private boolean enabled;

    private Role role;

    public User() {
        this.role = Role.USER;
    }
}

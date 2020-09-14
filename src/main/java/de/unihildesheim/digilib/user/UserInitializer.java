package de.unihildesheim.digilib.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserInitializer {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){

        if (repository.findUserByUsername("admin").isEmpty()) {
            User entity = new User();
            entity.setEnabled(true);
            entity.setUsername("admin");
            entity.setFirstname("Erika");
            entity.setLastname("Musterfrau");
            entity.setPassword(passwordEncoder.encode("password"));
            entity.setRole(Role.ADMIN);
            repository.save(entity);
        }
    }
}

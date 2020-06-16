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
        if (!repository.findUserByUsername("admin").isPresent()) {
            User entity = new User();
            entity.setEnabled(true);
            entity.setUsername("admin");
            entity.setFirstname("Erika");
            entity.setLastname("Musterfrau");
            entity.setPassword(passwordEncoder.encode("password"));
            entity.setRole(Role.ADMIN);
            repository.save(entity);
        }

        if (!repository.findUserByUsername("user").isPresent()) {
            User entity2 = new User();
            entity2.setEnabled(true);
            entity2.setUsername("user");
            entity2.setFirstname("Max");
            entity2.setLastname("Mustermann");
            entity2.setPassword(passwordEncoder.encode("1234"));
            repository.save(entity2);
        }


    }
}

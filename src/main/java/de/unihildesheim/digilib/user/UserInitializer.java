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
        repository.deleteAll();
        User entity = new User();
        entity.setEnabled(true);
        entity.setUsername("admin");
        entity.setPassword(passwordEncoder.encode("password"));
        repository.save(entity);
        User entity2 = new User();
        entity2.setEnabled(false);
        entity2.setUsername("user");
        entity2.setPassword(passwordEncoder.encode("1234"));
        repository.save(entity2);
    }
}
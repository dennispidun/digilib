package de.unihildesheim.digilib.user;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/api/user")
    public User user(Principal user) {
        return this.userRepository.findUserByUsername(user.getName()).orElseThrow(() -> new UsernameNotFoundException(user.getName()));
    }

}

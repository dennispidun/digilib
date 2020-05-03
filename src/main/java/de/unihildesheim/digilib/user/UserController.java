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
        if (user == null || user.getName() == null) {
            System.out.println("user = " + user);
            throw new UsernameNotFoundException("NULL");
        }

        String name = user.getName();
        return this.userRepository.findUserByUsername(name).orElseThrow(() -> new UsernameNotFoundException(user.getName()));
    }

}

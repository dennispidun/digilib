package de.unihildesheim.digilib.user;


import de.unihildesheim.digilib.book.model.ListBookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/api/users")
    public Page<User> getUsers(@RequestParam int pageNo,
                               @RequestParam int pageSize) {
        return this.userRepository
                .findAll(PageRequest.of(pageNo, pageSize));
    }

}

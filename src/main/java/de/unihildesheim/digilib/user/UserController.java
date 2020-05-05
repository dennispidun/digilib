package de.unihildesheim.digilib.user;


import de.unihildesheim.digilib.borrowing.BorrowerNotFoundException;
import de.unihildesheim.digilib.borrowing.model.Borrower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/users")
    public Page<User> getUsers(@RequestParam int pageNo,
                               @RequestParam int pageSize) {
        return this.userRepository
                .findAll(PageRequest.of(pageNo, pageSize));
    }

    @PatchMapping(value = "/api/users/{username}/enabled", consumes = MediaType.TEXT_PLAIN_VALUE)
    public User setEnabled(Principal loggedInUser, @PathVariable("username") String username, @RequestBody String enabled) {
        if (loggedInUser.getName().equalsIgnoreCase(username)) {
            throw new UserSelfEditException();
        }

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        user.setEnabled(enabled.equals("true"));
        return userRepository.save(user);
    }

}

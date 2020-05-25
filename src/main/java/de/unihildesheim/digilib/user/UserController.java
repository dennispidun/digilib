package de.unihildesheim.digilib.user;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
public class UserController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/api/user")
    public User user(Principal user) {
        if (user == null || user.getName() == null) {
            System.out.println("user = " + user);
            throw new UsernameNotFoundException("NULL");
        }

        String name = user.getName();
        User userByUsername = this.userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException(user.getName()));

        if (userByUsername.getRole() == null) {
            userByUsername.setRole(Role.USER);
        }

        return userByUsername;
    }

    @PostMapping("/api/users")
    public ResponseEntity<User> addUser(@RequestBody @Valid UserDto addUser) {
        User entity = new User();
        entity.setEnabled(true);
        entity.setUsername(addUser.getUsername());
        entity.setFirstname(addUser.getFirstname());
        entity.setLastname(addUser.getLastname());
        entity.setPassword(passwordEncoder.encode(addUser.getPassword()));
        entity.setRole(addUser.getRole());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userRepository.save(entity));
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

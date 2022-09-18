package nl.ocs.masterclass.spring.boot.web.controller;

import nl.ocs.masterclass.spring.boot.model.User;
import nl.ocs.masterclass.spring.boot.service.UserService;
import nl.ocs.masterclass.spring.boot.web.exception.UserAlreadyExistException;
import nl.ocs.masterclass.spring.boot.web.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserApi {

    private static final Logger LOG = LoggerFactory.getLogger(UserApi.class);

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, HttpServletRequest request) {
        if (userService.findUser(user.getName()).isPresent()) {
            LOG.warn("Duplicate user detection");
            throw new UserAlreadyExistException(user.getName());
        }

        user = userService.addUser(user);
        LOG.info("Created user {}", user);

        final URI userUri = ServletUriComponentsBuilder.fromRequestUri(request)
                .pathSegment(user.getName())
                .build()
                .toUri();

        return ResponseEntity.created(userUri).build();
    }

    @GetMapping
    public Iterable<User> getUsers() {
        LOG.info("Return all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{name}")
    public User getUser(@PathVariable @NotBlank String name) {
        LOG.info("Find user by name '{}'", name);

        return userService.findUser(name)
                .orElseThrow(() -> new UserNotFoundException(name));
    }
}

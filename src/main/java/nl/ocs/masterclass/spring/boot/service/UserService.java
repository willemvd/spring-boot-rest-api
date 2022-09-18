package nl.ocs.masterclass.spring.boot.service;

import nl.ocs.masterclass.spring.boot.model.User;
import nl.ocs.masterclass.spring.boot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUser(String name) {
        return userRepository.findByName(name);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}

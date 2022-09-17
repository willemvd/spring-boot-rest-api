package nl.ocs.masterclass.spring.boot.service;

import nl.ocs.masterclass.spring.boot.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final Map<String, User> users;

    public UserService() {
        users = new ConcurrentHashMap<>();
    }

    public User addUser(User user) {
        users.put(user.getName(), user);
        return user;
    }

    public Optional<User> findUser(String name) {
        return Optional.ofNullable(users.get(name));
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }
}

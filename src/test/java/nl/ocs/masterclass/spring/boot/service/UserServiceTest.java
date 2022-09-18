package nl.ocs.masterclass.spring.boot.service;

import nl.ocs.masterclass.spring.boot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static nl.ocs.masterclass.spring.boot.TestUtils.setupUser;
import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void addUser() {
        User givenUser = setupUser("name", "pass");

        final User user = userService.addUser(givenUser);

        assertThat(user).isEqualTo(givenUser);
    }

    @Test
    void findUserWithoutUser() {
        final Optional<User> user = userService.findUser("name");

        assertThat(user).isNotPresent();
    }

    @Test
    void findUserWithUser() {
        final User givenUser = setupUser("name", "pass");
        userService.addUser(givenUser);

        final Optional<User> user = userService.findUser("name");

        assertThat(user).isPresent();
        assertThat(user.get()).isEqualTo(givenUser);
    }

    @Test
    void getUsersWithoutUser() {
        final Collection<User> users = userService.getAllUsers();

        assertThat(users).isEmpty();
    }

    @Test
    void getUsersWithUser() {
        final User givenUser = setupUser("name", "pass");
        userService.addUser(givenUser);

        final Collection<User> users = userService.getAllUsers();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.iterator().next()).isEqualTo(givenUser);
    }
}
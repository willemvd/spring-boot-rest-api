package nl.ocs.masterclass.spring.boot.service;

import nl.ocs.masterclass.spring.boot.model.User;
import nl.ocs.masterclass.spring.boot.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static nl.ocs.masterclass.spring.boot.TestUtils.setupUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void addUser() {
        User givenUser = setupUser("name", "pass");
        User userFromService = setupUser("name", "pass");
        when(userRepository.save(givenUser)).thenReturn(userFromService);

        final User user = userService.addUser(givenUser);

        assertThat(user).isEqualTo(userFromService);

        verify(userRepository).save(givenUser);
    }

    @Test
    void findUserWithoutUser() {
        final Optional<User> user = userService.findUser("name");

        assertThat(user).isNotPresent();

        verify(userRepository).findByName("name");
    }

    @Test
    void findUserWithUser() {
        User givenUser = setupUser("name", "pass");
        when(userRepository.findByName("name")).thenReturn(Optional.of(givenUser));

        final Optional<User> user = userService.findUser("name");

        assertThat(user).isPresent();
        assertThat(user.get()).isEqualTo(givenUser);

        verify(userRepository).findByName("name");
    }

    @Test
    void getUsersWithoutUser() {
        final Iterable<User> users = userService.getAllUsers();

        assertThat(users).isEmpty();

        verify(userRepository).findAll();
    }

    @Test
    void getUsersWithUser() {
        final User givenUser = setupUser("name", "pass");
        when(userRepository.findAll()).thenReturn(List.of(givenUser));

        final Iterable<User> users = userService.getAllUsers();

        assertThat(users).isNotEmpty();
        assertThat(users.iterator().next()).isEqualTo(givenUser);
        assertThat(users.spliterator().getExactSizeIfKnown()).isEqualTo(1);
    }
}
package nl.ocs.masterclass.spring.boot.web.controller;

import nl.ocs.masterclass.spring.boot.model.User;
import nl.ocs.masterclass.spring.boot.service.UserService;
import nl.ocs.masterclass.spring.boot.web.exception.UserAlreadyExistException;
import nl.ocs.masterclass.spring.boot.web.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static nl.ocs.masterclass.spring.boot.TestUtils.setupUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApiTest {

    @InjectMocks
    private UserApi userApi;

    @Mock
    private UserService userService;

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(userService);
    }

    @Test
    void createUser() {
        final User givenUser = setupUser("name", "1234!");
        final MockHttpServletRequest request = MockMvcRequestBuilders.put("/user").buildRequest(new MockServletContext());
        when(userService.findUser(givenUser.getName())).thenReturn(Optional.empty());
        when(userService.addUser(givenUser)).thenReturn(givenUser);

        final ResponseEntity<?> createdUser = userApi.createUser(givenUser, request);

        assertThat(createdUser.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdUser.getHeaders()).containsKey("Location");
        assertThat(createdUser.getHeaders().get("Location").get(0)).isEqualTo(request.getRequestURL() + "/" + givenUser.getName());

        verify(userService).findUser(givenUser.getName());
        verify(userService).addUser(givenUser);
    }

    @Test
    void createUserAlreadyExists() {
        final User givenUser = setupUser("name", "1234!");
        final MockHttpServletRequest request = MockMvcRequestBuilders.put("/user").buildRequest(new MockServletContext());
        when(userService.findUser(givenUser.getName())).thenReturn(Optional.of(givenUser));

        assertThatExceptionOfType(UserAlreadyExistException.class).isThrownBy(() -> userApi.createUser(givenUser, request));

        verify(userService).findUser(givenUser.getName());
    }

    @Test
    void getUsers() {
        final User user1 = setupUser("user1", "p1");
        final User user2 = setupUser("user2", "p2");
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        final Collection<User> users = userApi.getUsers();

        assertThat(users).isNotNull();
        assertThat(users).isNotEmpty();
        assertThat(users).containsExactly(user1, user2);

        verify(userService).getAllUsers();
    }

    @Test
    void getUserFound() {
        final User user1 = setupUser("user1", "p1");
        when(userService.findUser("user1")).thenReturn(Optional.of(user1));

        final User result = userApi.getUser("user1");

        assertThat(result).isNotNull();
        assertThat(result).isSameAs(user1);

        verify(userService).findUser("user1");
    }

    @Test
    void getUserNotFound() {
        when(userService.findUser(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userApi.getUser("user1"));

        verify(userService).findUser("user1");
    }
}
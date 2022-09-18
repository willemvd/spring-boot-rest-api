package nl.ocs.masterclass.spring.boot.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ocs.masterclass.spring.boot.model.User;
import nl.ocs.masterclass.spring.boot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static nl.ocs.masterclass.spring.boot.TestUtils.setupUser;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserApi.class)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void validInputReturnsCreatedWithLocation() throws Exception {
        User givenUser = setupUser("user1", "pass1");
        when(userService.findUser(givenUser.getName())).thenReturn(Optional.empty());
        when(userService.addUser(givenUser)).thenReturn(givenUser);

        mockMvc.perform(
                put("/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(givenUser))
        ).andExpectAll(
                status().isCreated(),
                header().string("Location", "http://localhost/user/user1")
        );
    }

    @Test
    void duplicateUserReturnsConflict() throws Exception {
        User givenUser = setupUser("user1", "pass1");
        when(userService.findUser(givenUser.getName())).thenReturn(Optional.of(givenUser));

        mockMvc.perform(
                put("/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(givenUser))
        ).andExpect(
                status().isConflict()
        );
    }

    @Test
    void noInputReturnsBadRequest() throws Exception {
        mockMvc.perform(
                put("/user")
                        .contentType("application/json")
        ).andExpectAll(
                status().isBadRequest(),
                content().string("")
        );
    }

    @Test
    void badInputReturnsBadRequest() throws Exception {
        mockMvc.perform(
                put("/user")
                        .contentType("application/json")
                        .content("{}")
        ).andExpectAll(
                status().isBadRequest(),
                content().string("")
        );
    }

    @Test
    void getUsersWithUsersReturnsOkWithArray() throws Exception {
        final User user1 = setupUser("user1", "p1");
        final User user2 = setupUser("user2", "p2");
        final List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(
                get("/user")
        ).andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(users))
        );
    }

    @Test
    void getUsersWithNoUsersReturnsOkWithEmptyArray() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(
                get("/user")
        ).andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(Collections.emptyList()))
        );
    }

    @Test
    void getUserFoundReturnsOkWithContent() throws Exception {
        final User user1 = setupUser("user1", "p1");
        when(userService.findUser("user1")).thenReturn(Optional.of(user1));

        mockMvc.perform(
                get("/user/user1")
        ).andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(user1))
        );
    }

    @Test
    void getUserNotFoundReturnNotFound() throws Exception {
        when(userService.findUser(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(
                get("/user/user1")
        ).andExpectAll(
                status().isNotFound(),
                content().string("")
        );
    }
}
package nl.ocs.masterclass.spring.boot;

import nl.ocs.masterclass.spring.boot.model.User;

import java.util.UUID;

public final class TestUtils {
    private TestUtils() {

    }
    public static User setupUser(String name, String password) {
        return new User(UUID.randomUUID(), name, password);
    }
}

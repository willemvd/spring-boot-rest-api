package nl.ocs.masterclass.spring.boot.web.exception;

public class UserAlreadyExistException extends IllegalStateException {
    private final String name;

    public UserAlreadyExistException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

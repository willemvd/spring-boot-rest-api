package nl.ocs.masterclass.spring.boot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(length = 36)
    private UUID id;
    private @NotBlank String name;
    private @NotBlank String password;

    public User() {

    }

    public User(UUID id,
                @NotBlank String name,
                @NotBlank String password) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.name = name;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public @NotBlank String getName() {
        return name;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (User) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password);
    }

    @Override
    public String toString() {
        return "User[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "password=" + password + ']';
    }

}

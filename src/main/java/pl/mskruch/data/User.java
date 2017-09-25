package pl.mskruch.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class User {
    @Id
    Long id;
    @Index
    String email;
    Boolean enabled;

    public User(String email) {
        this.email = email;
        this.enabled = false;
    }

    User() {
    }

    public String getEmail()
    {
        return email;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + '}';
    }

    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}

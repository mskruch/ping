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

    public User(String email) {
        this.email = email;
    }

    User() {
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + '}';
    }
}

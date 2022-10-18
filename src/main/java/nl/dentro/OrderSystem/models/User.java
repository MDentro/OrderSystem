package nl.dentro.OrderSystem.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String userName;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}

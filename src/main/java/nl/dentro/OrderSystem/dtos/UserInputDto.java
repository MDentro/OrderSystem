package nl.dentro.OrderSystem.dtos;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserInputDto {
    @Size(min = 4, max = 15)
    private String userName;

    @Pattern(regexp="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,15})", message="Your password should contain 6 " +
            "tot 15 characters with at least one digit, one upper case letter, one lower case letter and one special " +
            "symbol (“@#$%”)")
    private String password;

    @Size(min = 1, max = 2)
    private String[] roles;

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

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}

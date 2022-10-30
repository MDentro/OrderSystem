package nl.dentro.OrderSystem.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

public class OrderInputDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Pattern(regexp= "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message="Invalid email")
    private String email;

    @NotBlank
    private String phoneNumber;

    @Size(min = 1, max = 20)
    private Collection<Long> productIds = new ArrayList<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Collection<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(Collection<Long> productIds) {
        this.productIds = productIds;
    }

}

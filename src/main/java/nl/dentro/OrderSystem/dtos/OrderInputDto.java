package nl.dentro.OrderSystem.dtos;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

public class OrderInputDto {

    private Long id;
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;


    private Collection<Long> productIds = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

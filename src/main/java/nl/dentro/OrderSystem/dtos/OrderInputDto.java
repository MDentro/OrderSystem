package nl.dentro.OrderSystem.dtos;

import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "OrderInputDto")
    @Size(min = 1, max = 20)
    @Valid
    List<ShoppingItemTransportInputDto> shoppingItemTransportInputDto = new ArrayList<>();

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

    public List<ShoppingItemTransportInputDto> getShoppingItemTransportInputDto() {
        return shoppingItemTransportInputDto;
    }

    public void setShoppingItemTransportInputDto(List<ShoppingItemTransportInputDto> shoppingItemTransportInputDto) {
        this.shoppingItemTransportInputDto = shoppingItemTransportInputDto;
    }
}

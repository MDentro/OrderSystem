package nl.dentro.OrderSystem.dtos;


import java.util.ArrayList;
import java.util.Collection;

public class OrderDto {
    private Long id;

    private Double totalPrice;
    private boolean paid;

    private UserDataDto userDataDto;

    Collection<ProductDto> productsDtoCollection = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public UserDataDto getUserDataDto() {
        return userDataDto;
    }

    public void setUserDataDto(UserDataDto userDataDto) {
        this.userDataDto = userDataDto;
    }

    public Collection<ProductDto> getProductsDtoCollection() {
        return productsDtoCollection;
    }

    public void setProductsDtoCollection(Collection<ProductDto> productsDtoCollection) {
        this.productsDtoCollection = productsDtoCollection;
    }
}

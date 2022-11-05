package nl.dentro.OrderSystem.dtos;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ShoppingItemTransportInputDto {

    @NotNull(message = "Id could not be empty")
    private Long productId;
    @Min(value=1, message="Minimum value should be 1")
    @Max(value=9, message="Maximum value should be 9")
    private int quantity;

    @ManyToOne
    OrderInputDto orderInputDto;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderInputDto getOrderInputDto() {
        return orderInputDto;
    }

    public void setOrderInputDto(OrderInputDto orderInputDto) {
        this.orderInputDto = orderInputDto;
    }
}

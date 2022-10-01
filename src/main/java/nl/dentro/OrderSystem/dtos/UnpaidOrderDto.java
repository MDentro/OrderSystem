package nl.dentro.OrderSystem.dtos;

public class UnpaidOrderDto {
    private Long id;

    private Double totalPrice;

    private String firstName;

    private String lastName;

    public UnpaidOrderDto(Long id, Double totalPrice, String firstName, String lastName) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UnpaidOrderDto() {
    }

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
}

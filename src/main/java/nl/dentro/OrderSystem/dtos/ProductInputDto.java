package nl.dentro.OrderSystem.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class ProductInputDto {
    private Long id;
    @Size(min = 3, max = 40)
    private String name;
    @Min(value = 1)
    private Double price;
    @Size(min = 3, max = 40)
    private String category;
    @Size(min = 3, max = 1000)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

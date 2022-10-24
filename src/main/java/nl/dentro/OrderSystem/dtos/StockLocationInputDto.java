package nl.dentro.OrderSystem.dtos;

import javax.validation.constraints.NotBlank;

public class StockLocationInputDto {
    private Long id;
    @NotBlank
    private String location;

    public StockLocationInputDto(String location) {
        this.location = location;
    }

    public StockLocationInputDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

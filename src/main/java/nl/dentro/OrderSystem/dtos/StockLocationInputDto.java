package nl.dentro.OrderSystem.dtos;

import javax.validation.constraints.NotBlank;

public class StockLocationInputDto {
    @NotBlank
    private String location;

    public StockLocationInputDto(String location) {
        this.location = location;
    }

    public StockLocationInputDto() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

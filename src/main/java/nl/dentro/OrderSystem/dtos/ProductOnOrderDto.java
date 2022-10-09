package nl.dentro.OrderSystem.dtos;

public class ProductOnOrderDto {
    private Long id;
    private String name;
    private Double price;
    private String category;

    private StockLocationDto stockLocationDto;

    private ImageUploadResponseDto imageUploadResponseDto;

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

    public StockLocationDto getStockLocationDto() {
        return stockLocationDto;
    }

    public void setStockLocationDto(StockLocationDto stockLocationDto) {
        this.stockLocationDto = stockLocationDto;
    }

    public ImageUploadResponseDto getImageUploadResponseDto() {
        return imageUploadResponseDto;
    }

    public void setImageUploadResponseDto(ImageUploadResponseDto imageUploadResponseDto) {
        this.imageUploadResponseDto = imageUploadResponseDto;
    }
}

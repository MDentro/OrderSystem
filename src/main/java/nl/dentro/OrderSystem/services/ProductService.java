package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ProductDto;
import nl.dentro.OrderSystem.dtos.ProductInputDto;
import nl.dentro.OrderSystem.dtos.ProductOnOrderDto;
import nl.dentro.OrderSystem.models.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsByCategory(String category);

    ProductDto getProductById(Long id);

    void assignStockLocationToProduct(Long id, Long input);

    void assignImageToProduct(String name, Long productId);

    List<ProductDto> fromProductListToDtoList(List<Product> products);

    ProductDto createProduct(ProductInputDto productInputDto);

    void updateProduct(ProductInputDto productInputDto, Long id);

    void deleteProduct(Long id);

    void setStockLocationAvailable(Long id);

    void saveChanges(Long id, Product updatedProduct);

    Product fromProductDto(ProductInputDto productInputDto);

    ProductDto toProductDto(Product product);

    ProductOnOrderDto toProductOnOrderDto(Product product);

    boolean availableProductId(Long id);

    boolean availableStockLocationId(Long id);

}

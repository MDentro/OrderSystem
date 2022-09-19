package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ProductDto;
import nl.dentro.OrderSystem.dtos.ProductInputDto;
import nl.dentro.OrderSystem.models.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsByCategory(String category);

    List<ProductDto> fromProductListToDtoList(List<Product> products);

    ProductDto createProduct(ProductInputDto productInputDto);

    void updateProduct(ProductInputDto productInputDto, Long id);

    void deleteProduct(Long id);

    void saveChanges(Long id, Product updatedProduct);

    Product fromProductDto(ProductInputDto productInputDto);

    ProductDto toProductDto(Product product);

    boolean availableProductId(Long id);
}

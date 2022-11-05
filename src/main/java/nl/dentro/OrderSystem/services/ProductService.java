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

    ProductDto createProduct(ProductInputDto productInputDto);

    void deleteProduct(Long id);

    void updateProduct(ProductInputDto productInputDto, Long id);

    void assignStockLocationToProduct(Long id, Long input);

    void assignImageToProduct(String name, Long productId);

    void saveChanges(Long id, Product updatedProduct);

    List<ProductDto> fromProductListToDtoList(List<Product> products);

    Product fromProductDto(ProductInputDto productInputDto);

    ProductDto toProductDto(Product product);

    ProductOnOrderDto toProductOnOrderDto(Product product, int quantity);

    Long searchIdToReleaseStockLocationIfNeeded(Product product);

    boolean availableProductId(Long id);
}

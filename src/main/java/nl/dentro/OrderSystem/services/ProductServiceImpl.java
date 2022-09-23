package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ProductDto;
import nl.dentro.OrderSystem.dtos.ProductInputDto;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.Product;
import nl.dentro.OrderSystem.repositories.ProductRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return fromProductListToDtoList(productList);
    }

    @Override
    public List<ProductDto> getAllProductsByCategory(String category) {
        List<Product> productList = productRepository.findAllProductsByCategoryEqualsIgnoreCase(category);
        return fromProductListToDtoList(productList);
    }

    @Override
    public ProductDto getProductById(Long id) {
        if (availableProductId(id)) {
            Product product = productRepository.findById(id).get();
            ProductDto productDto = toProductDto(product);
            return productDto;
        } else {
            throw new RecordNotFoundException("Could not find product with id: " + id + ".");
        }
    }


    @Override
    public List<ProductDto> fromProductListToDtoList(List<Product> products) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            ProductDto dto = toProductDto(product);
            productDtoList.add(dto);
        }
        return productDtoList;
    }

    @Override
    public ProductDto createProduct(ProductInputDto productInputDto) {
        Product product = fromProductDto(productInputDto);
        Product savedProduct = productRepository.save(product);
        ProductDto productDto = toProductDto(savedProduct);
        return productDto;
    }

    @Override
    public void updateProduct(ProductInputDto productInputDto, Long id) {
        if (availableProductId(id)) {
            Product updatedProduct = fromProductDto(productInputDto);
            saveChanges(id, updatedProduct);
        } else {
            throw new RecordNotFoundException("Could not find product with id: " + id + ".");
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new RecordNotFoundException("Could not find product with id: " + id + ".");
        }
    }

    @Override
    public void saveChanges(Long id, Product updatedProduct) {
        Product databaseProduct = productRepository.findById(id).get();
        if (!databaseProduct.getName().equals(updatedProduct.getName())) {
            databaseProduct.setName(updatedProduct.getName());
        }
        if (!databaseProduct.getPrice().equals(updatedProduct.getPrice())) {
            databaseProduct.setPrice(updatedProduct.getPrice());
        }
        if (!databaseProduct.getCategory().equals(updatedProduct.getCategory())) {
            databaseProduct.setCategory(updatedProduct.getCategory());
        }
        if (!databaseProduct.getDescription().equals(updatedProduct.getDescription())) {
            databaseProduct.setDescription(updatedProduct.getDescription());
        }
        productRepository.save(databaseProduct);
    }

    @Override
    public Product fromProductDto(ProductInputDto productInputDto) {
        var product = new Product();
        product.setName(productInputDto.getName());
        product.setPrice(productInputDto.getPrice());
        product.setCategory(productInputDto.getCategory());
        product.setDescription(productInputDto.getDescription());
        return product;
    }

    @Override
    public ProductDto toProductDto(Product product) {
        var dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        return dto;
    }

    @Override
    public boolean availableProductId(Long id) {
        return productRepository.findById(id).isPresent();
    }

}

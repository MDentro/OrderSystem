package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ProductDto;
import nl.dentro.OrderSystem.dtos.ProductInputDto;
import nl.dentro.OrderSystem.dtos.ProductOnOrderDto;
import nl.dentro.OrderSystem.exceptions.AvailableStockLocationNotFoundException;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.Product;
import nl.dentro.OrderSystem.models.StockLocation;
import nl.dentro.OrderSystem.repositories.ProductRepository;
import nl.dentro.OrderSystem.repositories.StockLocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final StockLocationRepository stockLocationRepository;

    private final StockLocationService stockLocationService;

    public ProductServiceImpl(ProductRepository productRepository, StockLocationRepository stockLocationRepository, StockLocationService stockLocationService) {
        this.productRepository = productRepository;
        this.stockLocationRepository = stockLocationRepository;
        this.stockLocationService = stockLocationService;
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
    public void assignStockLocationToProduct(Long id, Long input) {
        if (availableProductId(id) && availableStockLocationId(input)) {
            StockLocation stockLocation = stockLocationRepository.findById(input).get();
            if (stockLocation.isAvailable()) {
                Product product = productRepository.findById(id).get();
                product.setStockLocation(stockLocation);
                stockLocation.setAvailable(false);
                productRepository.save(product);
                stockLocationRepository.save(stockLocation);
            } else {
                throw new AvailableStockLocationNotFoundException("Stock location  with id: " + input + " is already in use.");
            }

        } else if (!availableProductId(id) && !availableStockLocationId(input)) {
            throw new RecordNotFoundException("Could not find product with id: " + id + " and could not find stock location with id: " + input + ".");
        } else if (!availableProductId(id)) {
            throw new RecordNotFoundException("Could not find product with id: " + id + ".");
        } else if (!availableStockLocationId(input)) {
            throw new RecordNotFoundException("Could not find stock location with id: " + input + ".");
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
        if (availableProductId(id)) {
            setStockLocationAvailable(id);
            productRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Could not find product with id: " + id + ".");
        }
    }

    @Override
    public void setStockLocationAvailable(Long id) {
        Product product = productRepository.findById(id).get();
        StockLocation stockLocation = product.getStockLocation();
        if (stockLocation != null) {
            stockLocation.setAvailable(true);
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
    public ProductOnOrderDto toProductOnOrderDto(Product product) {
        var dto = new ProductOnOrderDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        if(product.getStockLocation() != null) {
            dto.setStockLocationDto(stockLocationService.toStockLocationDto(product.getStockLocation()));
        }

        return dto;
    }

    @Override
    public boolean availableProductId(Long id) {
        return productRepository.findById(id).isPresent();
    }

    @Override
    public boolean availableStockLocationId(Long id) {
        return stockLocationRepository.findById(id).isPresent();
    }
}

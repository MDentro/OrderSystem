package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ProductDto;
import nl.dentro.OrderSystem.dtos.ProductInputDto;
import nl.dentro.OrderSystem.dtos.ProductOnOrderDto;
import nl.dentro.OrderSystem.exceptions.AvailableStockLocationNotFoundException;
import nl.dentro.OrderSystem.exceptions.RecordCanNotBeDeletedException;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.Image;
import nl.dentro.OrderSystem.models.Product;
import nl.dentro.OrderSystem.models.StockLocation;
import nl.dentro.OrderSystem.repositories.ImageRepository;
import nl.dentro.OrderSystem.repositories.ProductRepository;
import nl.dentro.OrderSystem.repositories.StockLocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final StockLocationRepository stockLocationRepository;

    private final StockLocationService stockLocationService;

    private final ImageRepository imageRepository;

    private final ImageService imageService;

    private final OrderProductService orderProductService;

    public ProductServiceImpl(ProductRepository productRepository, StockLocationRepository stockLocationRepository, StockLocationService stockLocationService, ImageRepository imageRepository, ImageService imageService, OrderProductService orderProductService) {
        this.productRepository = productRepository;
        this.stockLocationRepository = stockLocationRepository;
        this.stockLocationService = stockLocationService;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
        this.orderProductService = orderProductService;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return fromProductListToDtoList(productList);
    }

    @Override
    public List<ProductDto> getAllProductsByCategory(String category) {
        List<Product> productList = productRepository.findAllProductsByCategoryEqualsIgnoreCase(category);
        if (productList.size() == 0) {
            throw new RecordNotFoundException("Could not find products with category " + category);
        }
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
    public ProductDto createProduct(ProductInputDto productInputDto) {
        Product product = fromProductDto(productInputDto);
        Product savedProduct = productRepository.save(product);
        ProductDto productDto = toProductDto(savedProduct);
        return productDto;
    }

    @Override
    public void deleteProduct(Long id) {
        if (availableProductId(id)) {
            Product product = productRepository.findById(id).get();
            if (orderProductService.isProductOrdered(id)) {
                throw new RecordCanNotBeDeletedException("Product with id: " + id + " is used on an order and cannot be deleted.");
            } else {
                if (searchIdToReleaseStockLocationIfNeeded(product) != -1L) {
                    stockLocationService.setStockLocationAvailable(searchIdToReleaseStockLocationIfNeeded(product));
                    productRepository.deleteById(id);
                }
                if (product.getFile() != null) {
                    imageService.deleteImage(product.getFile().getFileName());
                }
            }
        } else {
            throw new RecordNotFoundException("Could not find product with id: " + id + ".");
        }
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
    public void assignStockLocationToProduct(Long id, Long input) {
        if (availableProductId(id) && stockLocationService.availableStockLocationId(input)) {
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

        } else if (!availableProductId(id) && !stockLocationService.availableStockLocationId(input)) {
            throw new RecordNotFoundException("Could not find product with id: " + id + " and could not find stock location with id: " + input + ".");
        } else if (!availableProductId(id)) {
            throw new RecordNotFoundException("Could not find product with id: " + id + ".");
        } else if (!stockLocationService.availableStockLocationId(input)) {
            throw new RecordNotFoundException("Could not find stock location with id: " + input + ".");
        }
    }

    @Override
    public void assignImageToProduct(String name, Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<Image> image = imageRepository.findByFileName(name);
        String fileName = "";

        if (optionalProduct.isPresent() && image.isPresent()) {
            Image photo = image.get();
            Product product = optionalProduct.get();
            if (product.getFile() != null) {
                fileName = product.getFile().getFileName();
            }

            product.setFile(photo);
            productRepository.save(product);

            if (!fileName.equals(photo.getFileName()) && !fileName.equals("")) {
                imageService.deleteImage(fileName);
            }
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
    public List<ProductDto> fromProductListToDtoList(List<Product> products) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            ProductDto dto = toProductDto(product);
            productDtoList.add(dto);
        }
        return productDtoList;
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
        if (product.getFile() != null) {
            dto.setImageDto(imageService.toImageDTO(product.getFile().getFileName(), product.getFile().getContentType(), product.getFile().getUrl()));
        }
        return dto;
    }

    @Override
    public ProductOnOrderDto toProductOnOrderDto(Product product) {
        var dto = new ProductOnOrderDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        if (product.getStockLocation() != null) {
            dto.setStockLocationDto(stockLocationService.toStockLocationDto(product.getStockLocation()));
        }
        if (product.getFile() != null) {
            dto.setImageDto(imageService.toImageDTO(product.getFile().getFileName(), product.getFile().getContentType(), product.getFile().getUrl()));
        }
        return dto;
    }

    @Override
    public Long searchIdToReleaseStockLocationIfNeeded(Product product) {
        StockLocation stockLocation = product.getStockLocation();
        if (stockLocation != null) {
            return stockLocation.getId();
        }
        return -1L;
    }

    @Override
    public boolean availableProductId(Long id) {
        return productRepository.findById(id).isPresent();
    }
}

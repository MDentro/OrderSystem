package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.ProductDto;
import nl.dentro.OrderSystem.dtos.ProductInputDto;
import nl.dentro.OrderSystem.dtos.ProductOnOrderDto;
import nl.dentro.OrderSystem.exceptions.AvailableStockLocationNotFoundException;
import nl.dentro.OrderSystem.exceptions.RecordCanNotBeDeletedException;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.*;
import nl.dentro.OrderSystem.repositories.ImageRepository;
import nl.dentro.OrderSystem.repositories.ProductRepository;
import nl.dentro.OrderSystem.repositories.StockLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;

    @Mock
    StockLocationRepository stockLocationRepository;

    @Mock
    OrderProductServiceImpl orderProductService;

    @Mock
    ImageServiceImpl imageService;

    @Mock
    ImageRepository imageRepository;

    @Mock
    StockLocationServiceImpl stockLocationService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    ArgumentCaptor<Product> argumentCaptor;

    Product product1;

    Product product2;

    Product product3;

    Product product1Updated;

    ProductDto product1Dto;

    ProductInputDto product1InputDto;

    Image product1Image;

    Image product2Image;

    StockLocation product1Stocklocation;

    StockLocation product2Stocklocation;

    StockLocation toBeAssignedStockLocation;

    StockLocation unAvailableStockLocation;

    Collection<OrderProduct> orderProductCollectionEmpty = new ArrayList<>();

    Collection<OrderProduct> orderProductCollection = new ArrayList<>();

    OrderProduct orderProduct;

    Order order;

    UserData userData;

    @BeforeEach
    void setUp() {
        product1 = new Product(1001L, "APTITLIG", 17.99, "cooking", "The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.");
        product2 = new Product(1002L, "KAVALKAD", 4.99, "cooking", "The pan s low weight makes it easy to handle when filled with food. Made from aluminium, which spreads heat evenly and energy efficiently, and makes it easier to regulate heat so the food does not burn and stick. With Teflon® Classic non-stick coating that makes cooking and cleaning easy. Easy grip handle makes the pan easy to lift.");
        product3 = new Product(1006L, "VARDAGEN", 5.99, "baking", "Help you get the right amount of spices, flour or other flavourings when cooking or baking. Take up little storage space since the dimensions fit in each other. Feel free to hang them on a hook over the kitchen worktop so you always have them close at hand.");

        product1Updated = new Product(1001L, "updatedName", 20.00, "baking", "UpdatedDescription.");

        product1Dto = new ProductDto(1001L, "APTITLIG", 17.99, "cooking", "The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.");

        product1InputDto = new ProductInputDto("APTITLIG", 17.99, "cooking", "The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.");

        product1Image = new Image("aptitlig.jpg", "image/jpeg", "http://localhost:8090/download/aptitlig.jpg");
        product2Image = new Image("kavalkad.jpg", "image/jpeg", "http://localhost:8090/download/kavalkad.jpg");

        product1Stocklocation = new StockLocation(100L, "05.12.2", false);
        product2Stocklocation = new StockLocation(102L, "06.12.2", false);
        toBeAssignedStockLocation = new StockLocation(103L, "06.12.3", true);
        unAvailableStockLocation = new StockLocation(104L, "07.12.2", false);

        order = new Order();
        order.setId(300L);
        order.setPaid(false);
        order.setTotalPrice(27.97);
        order.setUserData(userData);
        orderProduct = new OrderProduct(order,product3);

        orderProductCollection.add(orderProduct);

        userData = new UserData(200L,"Charles", "Darwin", "charles@darwin.com", "06-12345678" );

    }


    @Test
    void shouldReturnAllProductsWhenRequested() {
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<ProductDto> productsFound = productService.getAllProducts();

        assertEquals(product1.getName(), productsFound.get(0).getName());
        assertEquals(product1.getPrice(), productsFound.get(0).getPrice());
        assertEquals(product1.getCategory(), productsFound.get(0).getCategory());
        assertEquals(product1.getDescription(), productsFound.get(0).getDescription());
        assertEquals(product2.getName(), productsFound.get(1).getName());
        assertEquals(product2.getPrice(), productsFound.get(1).getPrice());
        assertEquals(product2.getCategory(), productsFound.get(1).getCategory());
        assertEquals(product2.getDescription(), productsFound.get(1).getDescription());
    }

    @Test
    void shouldReturnAllProductsByCategoryWhenCategoryIsGiven() {
        String category = "cooking";
        when(productRepository.findAllProductsByCategoryEqualsIgnoreCase(category)).thenReturn(List.of(product1, product2));

        List<ProductDto> productsFound = productService.getAllProductsByCategory(category);

        assertEquals(product1.getName(), productsFound.get(0).getName());
        assertEquals(product1.getPrice(), productsFound.get(0).getPrice());
        assertEquals(product1.getCategory(), productsFound.get(0).getCategory());
        assertEquals(product1.getDescription(), productsFound.get(0).getDescription());
        assertEquals(product2.getName(), productsFound.get(1).getName());
        assertEquals(product2.getPrice(), productsFound.get(1).getPrice());
        assertEquals(product2.getCategory(), productsFound.get(1).getCategory());
        assertEquals(product2.getDescription(), productsFound.get(1).getDescription());
    }

    @Test
    void shouldReturnExceptionWhenCategoryDoesNotExist() {
        String category = "cook";

        assertThrows(RecordNotFoundException.class, () -> productService.getAllProductsByCategory(category));
    }

    @Test
    void shouldReturnProductDtoWhenProductIdIsGiven() {
        Long id = 1001L;
        when(productRepository.findById(id)).thenReturn(Optional.of(product1));

        Product product = productRepository.findById(id).get();
        ProductDto productDto = productService.getProductById(id);

        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getPrice(), productDto.getPrice());
        assertEquals(product.getCategory(), productDto.getCategory());
        assertEquals(product.getDescription(), productDto.getDescription());
    }


    @Test
    void shouldReturnExceptionWhenProductIdDoesNotExist() {
        Long id = null;

        assertThrows(RecordNotFoundException.class, () -> productService.getProductById(id));
    }

    @Test
    void shouldCreateProductWhenProductDtoIsGiven() {
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product1);

        productService.createProduct(product1InputDto);
        verify(productRepository, times(1)).save(argumentCaptor.capture());
        Product product = argumentCaptor.getValue();

        assertEquals(product1Dto.getName(), product.getName());
        assertEquals(product1Dto.getPrice(), product.getPrice());
        assertEquals(product1Dto.getCategory(), product.getCategory());
        assertEquals(product1Dto.getDescription(), product.getDescription());
    }

    @Test
    void shouldDeleteProductWhenIdIsGiven() {
        product3.setStockLocation(product2Stocklocation);
        product3.setOrderProduct(orderProductCollectionEmpty);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product3));

        productService.deleteProduct(anyLong());

        verify(productRepository).deleteById(anyLong());
    }

    @Test
    void shouldDeleteProductImageWhenProductIsDeleted() {
        product1.setFile(product1Image);
        product1.setStockLocation(product1Stocklocation);
        product1.setOrderProduct(orderProductCollectionEmpty);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product1));

        productService.deleteProduct(anyLong());

        verify(productRepository).deleteById(anyLong());
    }

    @Test
    void shouldReturnRecordCanNotBeDeletedExceptionWhenProductIsOrdered() {
        Long id = 1006L;
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product3));
        product3.setOrderProduct(orderProductCollection);

        assertThrows(RecordCanNotBeDeletedException.class, () -> productService.deleteProduct(id));
    }


    @Test
    void shouldReturnExceptionWhenProductDeletingProductIfProductIdDoesNotExist() {
        Long id = null;

        assertThrows(RecordNotFoundException.class, () -> productService.deleteProduct(id));
    }


    @Test
    void shouldReturnUpdatedProductWhenNewDetailsAreGiven() {
        when(productRepository.findById(1001L)).thenReturn(Optional.of(product1));

        productService.updateProduct(product1InputDto, 1001L);
    }

    @Test
    void shouldReturnExceptionWhenProductIdToBeChangedDoesNotExist() {
        Long id = null;

        assertThrows(RecordNotFoundException.class, () -> productService.updateProduct(product1InputDto, id));
    }


    @Test
    void shouldSaveChangesWhenNewProductDataIsUpdated() {
        when(productRepository.findById(1001L)).thenReturn(Optional.of(product1));

        productService.saveChanges(1001L, product1Updated);

        assertEquals(product1Updated.getName(), product1.getName());
        assertEquals(product1Updated.getPrice(), product1.getPrice());
        assertEquals(product1Updated.getCategory(), product1.getCategory());
        assertEquals(product1Updated.getDescription(), product1.getDescription());
    }


    @Test
    void shouldReturnIdWhenStockLocationIsNotAssignedToProduct() {
        Long id = 1001L;
        product1.setStockLocation(product1Stocklocation);
        when(productRepository.findById(id)).thenReturn(Optional.of(product1));

        Long result = productService.searchIdToReleaseStockLocationIfNeeded(product1);

        assertEquals(100L, result);
    }


    @Test
    void shouldReturnMinusOneWhenStockLocationIsNotAssignedToProduct() {
        Long id = 1001L;
        when(productRepository.findById(id)).thenReturn(Optional.of(product1));

        Long result = productService.searchIdToReleaseStockLocationIfNeeded(product1);

        assertEquals(-1L, result);
    }

    @Test
    void shouldAssignStockLocationToProductWhenBothAreAvailable() {
        Long id = 1001L;
        Long input = 103L;
        when(productRepository.findById(id)).thenReturn(Optional.of(product1));
        when(stockLocationService.availableStockLocationId(input)).thenReturn(true);
        when(stockLocationRepository.findById(input)).thenReturn(Optional.of(toBeAssignedStockLocation));

        productService.assignStockLocationToProduct(id, input);

        assertEquals(product1.getStockLocation().getLocation(), toBeAssignedStockLocation.getLocation());
        assertEquals(product1.getStockLocation().isAvailable(), toBeAssignedStockLocation.isAvailable());
    }

    @Test
    void shouldSetOldStockLocationAvailableWhenAssigningNewStockLocationToProduct() {
        Long id = 1001L;
        Long input = 103L;
        product1.setStockLocation(product1Stocklocation);
        when(productRepository.findById(id)).thenReturn(Optional.of(product1));
        when(stockLocationService.availableStockLocationId(input)).thenReturn(true);
        when(stockLocationRepository.findById(input)).thenReturn(Optional.of(toBeAssignedStockLocation));

        productService.assignStockLocationToProduct(id, input);

        assertEquals(product1.getStockLocation().getLocation(), toBeAssignedStockLocation.getLocation());
        assertEquals(product1.getStockLocation().isAvailable(), toBeAssignedStockLocation.isAvailable());
    }

    @Test
    void shouldReturnExceptionWhenTryingToAssignUnAvailableStockLocationToProductId() {
        Long id = 1001L;
        Long input = 103L;
        when(productRepository.findById(id)).thenReturn(Optional.of(product1));
        when(stockLocationService.availableStockLocationId(input)).thenReturn(true);
        when(stockLocationRepository.findById(input)).thenReturn(Optional.of(unAvailableStockLocation));

        assertThrows(AvailableStockLocationNotFoundException.class, () -> productService.assignStockLocationToProduct(id, input));
    }


    @Test
    void shouldReturnExceptionWhenTryingToAssignNotExistingStockLocationToProductIfProductIdThatDoesNotExist() {
        Long id = null;
        Long input = null;

        assertThrows(RecordNotFoundException.class, () -> productService.assignStockLocationToProduct(id, input));
    }


    @Test
    void shouldReturnExceptionWhenTryingToAssignStockLocationToProductIfProductIdDoesNotExist() {
        Long id = null;
        Long input = 103L;
        when(stockLocationRepository.findById(input)).thenReturn(Optional.of(product1Stocklocation));
        when(stockLocationService.availableStockLocationId(input)).thenReturn(true);
        when(stockLocationRepository.findById(input)).thenReturn(Optional.of(toBeAssignedStockLocation));

        assertThrows(RecordNotFoundException.class, () -> productService.assignStockLocationToProduct(id, input));
    }

    @Test
    void shouldReturnExceptionWhenTryingToAssignNotExistingStockLocationToProductId() {
        Long id = 1001L;
        Long input = null;
        when(productRepository.findById(id)).thenReturn(Optional.of(product1));
        when(stockLocationRepository.findById(input)).thenReturn(null);

        assertThrows(RecordNotFoundException.class, () -> productService.assignStockLocationToProduct(id, input));
    }

    @Test
    void shouldAssignImageToProductWhenGiven() {
        String name = product1Image.getFileName();
        Long productId = 1001L;
        product1.setFile(null);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));
        when(imageRepository.findByFileName(name)).thenReturn(Optional.of(product1Image));

        productService.assignImageToProduct(name, productId);

        assertEquals(product1.getFile().getFileName(), product1Image.getFileName());
        assertEquals(product1.getFile().getContentType(), product1Image.getContentType());
        assertEquals(product1.getFile().getUrl(), product1Image.getUrl());
    }

    @Test
    void shouldAssignImageToProductWhenGivenAndDeleteExistingImage() {
        String name = product1Image.getFileName();
        Long productId = 1001L;
        product1.setFile(product2Image);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));
        when(imageRepository.findByFileName(name)).thenReturn(Optional.of(product1Image));

        productService.assignImageToProduct(name, productId);

        assertEquals(product1.getFile().getFileName(), product1Image.getFileName());
        assertEquals(product1.getFile().getContentType(), product1Image.getContentType());
        assertEquals(product1.getFile().getUrl(), product1Image.getUrl());
    }

    @Test
    void shouldAssignImageToProductWhenGivenAndShouldNotDeleteExistingImageWithTheSameFileNameDueToOverwriting() {
        String name = product1Image.getFileName();
        Long productId = 1001L;
        product1.setFile(product1Image);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));
        when(imageRepository.findByFileName(name)).thenReturn(Optional.of(product1Image));

        productService.assignImageToProduct(name, productId);

        assertEquals(product1.getFile().getFileName(), product1Image.getFileName());
        assertEquals(product1.getFile().getContentType(), product1Image.getContentType());
        assertEquals(product1.getFile().getUrl(), product1Image.getUrl());
    }

    @Test
    void shouldReturnExceptionWhenTryingToAssignAnImageToAProductIfProductIdDoesNotExist() {
        String name = product1Image.getFileName();
        Long productId = null;

        when(imageRepository.findByFileName(name)).thenReturn(Optional.of(product1Image));

        assertThrows(RecordNotFoundException.class, () -> productService.assignImageToProduct(name, productId));
    }

    @Test
    void shouldReturnProductDtoWhenProductIsGiven() {
        product1.setFile(product1Image);

        ProductDto result = productService.toProductDto(product1);

        assertEquals(product1Dto.getName(), result.getName());
        assertEquals(product1Dto.getPrice(), result.getPrice());
        assertEquals(product1Dto.getCategory(), result.getCategory());
        assertEquals(product1Dto.getDescription(), result.getDescription());
    }

    @Test
    void shouldReturnProductOnOrderDtoWhenProductIsGiven() {
        product1.setFile(product1Image);
        product1.setStockLocation(product1Stocklocation);

        ProductOnOrderDto result = productService.toProductOnOrderDto(product1);

        assertEquals(product1Dto.getName(), result.getName());
        assertEquals(product1Dto.getPrice(), result.getPrice());
        assertEquals(product1Dto.getCategory(), result.getCategory());
    }


    @Test
    void shouldReturnProductWhenProductDtoIsGiven() {
        Product result = productService.fromProductDto(product1InputDto);

        assertEquals(product1InputDto.getName(), result.getName());
        assertEquals(product1InputDto.getPrice(), result.getPrice());
        assertEquals(product1InputDto.getCategory(), result.getCategory());
        assertEquals(product1InputDto.getDescription(), result.getDescription());
    }


    @Test
    void shouldReturnTrueIfProductIdIsAvailable() {
        Long id = 1001L;
        when(productRepository.findById(id)).thenReturn(Optional.of(product1));

        Boolean found = productService.availableProductId(id);

        assertTrue(found);
    }


    @Test
    void shouldReturnFalseIfProductIdIsNotAvailable() {
        Long id = null;

        Boolean found = productService.availableProductId(id);

        assertFalse(found);
    }

}
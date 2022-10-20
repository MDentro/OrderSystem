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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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

    Product aptitlig;

    Product aptitligUpdated;
    Product kavalkad;
    Product mixtur;
    Product hammabak;
    Product magasin;
    Product vardagen;

    ProductDto aptitligDto;

    ProductInputDto aptitligInputDto;
    ProductDto kavalkadDto;

    Image aptitligImage;

    StockLocation aptitligStocklocation;
    StockLocation vardagenStocklocation;

    StockLocation toBeAssignedStockLocation;

    StockLocation unAvailableStockLocation;


    @BeforeEach
    void setUp() {
        aptitlig = new Product(1001L, "APTITLIG", 17.99, "cooking", "The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.");
        kavalkad = new Product(1002L, "KAVALKAD", 4.99, "cooking", "The pan s low weight makes it easy to handle when filled with food. Made from aluminium, which spreads heat evenly and energy efficiently, and makes it easier to regulate heat so the food does not burn and stick. With Teflon® Classic non-stick coating that makes cooking and cleaning easy. Easy grip handle makes the pan easy to lift.");
        mixtur = new Product(1003L, "MIXTUR", 4.99, "cooking", "Timeless oven dish in clear glass that you can take directly from the oven and put on the table. Dishwasher-, oven- and microwave-safe. Two small handles make it easy to lift and carry – with potholders as protection of course.");
        hammabak = new Product(1004L, "HEMMABAK", 9.99, "baking", "This muffin tin is perfect for muffins, cupcakes, quiches or even bread. Made of durable steel with non-stick coating to make your pastries and food easy to loosen from the tin. The steel distributes the heat evenly, which makes your baking soft and scrumptious on the inside and gives it a nice golden-brown surface.");
        magasin = new Product(1005L, "MAGASIN", 4.99, "baking", "Helps you roll out the dough when baking buns, bread or creating a tasty pizza base. Simple to clean – just wipe dry with a damp cloth.");
        vardagen = new Product(1006L, "VARDAGEN", 5.99, "baking", "Help you get the right amount of spices, flour or other flavourings when cooking or baking. Take up little storage space since the dimensions fit in each other. Feel free to hang them on a hook over the kitchen worktop so you always have them close at hand.");

        aptitligUpdated = new Product(1001L, "updatedName", 20.00, "baking", "UpdatedDescription.");

        aptitligDto = new ProductDto(1001L, "APTITLIG", 17.99, "cooking", "The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.");
        kavalkadDto = new ProductDto(1002L, "KAVALKAD", 4.99, "cooking", "The pan s low weight makes it easy to handle when filled with food. Made from aluminium, which spreads heat evenly and energy efficiently, and makes it easier to regulate heat so the food does not burn and stick. With Teflon® Classic non-stick coating that makes cooking and cleaning easy. Easy grip handle makes the pan easy to lift.");

        aptitligInputDto = new ProductInputDto("APTITLIG", 17.99, "cooking", "The chopping board collects meat and fruit juice in the milled groove and prevents it from spilling on to your worktop. You can easily turn the chopping board and use both sides when you prepare food, because it has easy-to-grip slanted edges. Made of bamboo, which is an easy-care, hardwearing natural material that is also gentle on your knives.");

        aptitligImage = new Image("aptitlig.jpg", "image/jpeg", "http://localhost:8090/download/aptitlig.jpg");

        aptitligStocklocation = new StockLocation(100L, "05.12.2", false);
        vardagenStocklocation = new StockLocation(102L, "06.12.2", false);
        toBeAssignedStockLocation = new StockLocation(103L, "06.12.3", true);
        unAvailableStockLocation = new StockLocation(104L, "07.12.2", false);
    }


    @Test
    void shouldReturnAllProductsWhenRequested() {
        when(productRepository.findAll()).thenReturn(List.of(aptitlig, kavalkad));

        List<ProductDto> productsFound = productService.getAllProducts();

        assertEquals(aptitlig.getName(), productsFound.get(0).getName());
        assertEquals(aptitlig.getPrice(), productsFound.get(0).getPrice());
        assertEquals(aptitlig.getCategory(), productsFound.get(0).getCategory());
        assertEquals(aptitlig.getDescription(), productsFound.get(0).getDescription());
        assertEquals(kavalkad.getName(), productsFound.get(1).getName());
        assertEquals(kavalkad.getPrice(), productsFound.get(1).getPrice());
        assertEquals(kavalkad.getCategory(), productsFound.get(1).getCategory());
        assertEquals(kavalkad.getDescription(), productsFound.get(1).getDescription());
    }

    @Test
    void shouldReturnAllProductsByCategoryWhenCategoryIsGiven() {
        String category = "cooking";

        when(productRepository.findAllProductsByCategoryEqualsIgnoreCase(category)).thenReturn(List.of(aptitlig, kavalkad, magasin, vardagen));

        List<ProductDto> productsFound = productService.getAllProductsByCategory(category);

        assertEquals(aptitlig.getName(), productsFound.get(0).getName());
        assertEquals(aptitlig.getPrice(), productsFound.get(0).getPrice());
        assertEquals(aptitlig.getCategory(), productsFound.get(0).getCategory());
        assertEquals(aptitlig.getDescription(), productsFound.get(0).getDescription());
        assertEquals(kavalkad.getName(), productsFound.get(1).getName());
        assertEquals(kavalkad.getPrice(), productsFound.get(1).getPrice());
        assertEquals(kavalkad.getCategory(), productsFound.get(1).getCategory());
        assertEquals(kavalkad.getDescription(), productsFound.get(1).getDescription());
    }

    @Test
    void shouldReturnExceptionWhenCategoryDoesNotExist() {
        String category = "cook";

        assertThrows(RecordNotFoundException.class, () -> productService.getAllProductsByCategory(category));
    }

    @Test
    void shouldReturnProductDtoWhenProductIdIsGiven() {
        Long id = 1001L;
        when(productRepository.findById(id)).thenReturn(Optional.of(aptitlig));

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
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(aptitlig);
        productService.createProduct(aptitligInputDto);
        verify(productRepository, times(1)).save(argumentCaptor.capture());
        Product product = argumentCaptor.getValue();

        assertEquals(aptitligDto.getName(), product.getName());
        assertEquals(aptitligDto.getPrice(), product.getPrice());
        assertEquals(aptitligDto.getCategory(), product.getCategory());
        assertEquals(aptitligDto.getDescription(), product.getDescription());
    }

    @Test
    void shouldDeleteProductWhenIdIsGiven() {
        vardagen.setStockLocation(vardagenStocklocation);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(vardagen));
        when(orderProductService.isProductOrdered(anyLong())).thenReturn(false);

        productService.deleteProduct(anyLong());

        verify(productRepository).deleteById(anyLong());
    }

    @Test
    void shouldDeleteProductImageWhenProductIsDeleted() {
        aptitlig.setFile(aptitligImage);
        aptitlig.setStockLocation(aptitligStocklocation);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(aptitlig));

        productService.deleteProduct(anyLong());

        verify(productRepository).deleteById(anyLong());
    }

    @Test
    void shouldReturnRecordCanNotBeDeletedExceptionWhenProductIsOrdered() {
        Long id = 1006L;
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(vardagen));
        when(orderProductService.isProductOrdered(id)).thenReturn(true);

        assertThrows(RecordCanNotBeDeletedException.class, () -> productService.deleteProduct(id));
    }


    @Test
    void shouldReturnExceptionWhenProductDeletingProductIfProductIdDoesNotExist() {
        Long id = null;

        assertThrows(RecordNotFoundException.class, () -> productService.deleteProduct(id));
    }


    @Test
    void shouldReturnUpdatedProductWhenNewDetailsAreGiven() {
        when(productRepository.findById(1001L)).thenReturn(Optional.of(aptitlig));
        productService.updateProduct(aptitligInputDto, 1001L);
    }

    @Test
    void shouldReturnExceptionWhenProductIdToBeChangedDoesNotExist() {
        Long id = null;

        assertThrows(RecordNotFoundException.class, () -> productService.updateProduct(aptitligInputDto, id));
    }


    @Test
    void shouldSaveChangesWhenNewProductDataIsUpdated() {
        when(productRepository.findById(1001L)).thenReturn(Optional.of(aptitlig));

        productService.saveChanges(1001L, aptitligUpdated);

        assertEquals(aptitligUpdated.getName(), aptitlig.getName());
        assertEquals(aptitligUpdated.getPrice(), aptitlig.getPrice());
        assertEquals(aptitligUpdated.getCategory(), aptitlig.getCategory());
        assertEquals(aptitligUpdated.getDescription(), aptitlig.getDescription());
    }


    @Test
    void shouldReturnIdWhenStockLocationIsNotAssignedToProduct() {
        Long id = 1001L;
        aptitlig.setStockLocation(aptitligStocklocation);
        when(productRepository.findById(id)).thenReturn(Optional.of(aptitlig));

        Long result = productService.searchIdToReleaseStockLocationIfNeeded(aptitlig.getId());

        assertEquals(100L, result);
    }


    @Test
    void shouldReturnMinusOneWhenStockLocationIsNotAssignedToProduct() {

        Long id = 1001L;
        when(productRepository.findById(id)).thenReturn(Optional.of(aptitlig));
        Long result = productService.searchIdToReleaseStockLocationIfNeeded(aptitlig.getId());

        assertEquals(-1L, result);

    }

    @Test
    void shouldAssignStockLocationToProductWhenBothAreAvailable() {
        Long id = 1001L;
        Long input = 103L;
        when(productRepository.findById(id)).thenReturn(Optional.of(aptitlig));
        when(stockLocationService.availableStockLocationId(input)).thenReturn(true);
        when(stockLocationRepository.findById(input)).thenReturn(Optional.of(toBeAssignedStockLocation));

        productService.assignStockLocationToProduct(id, input);

        assertEquals(aptitlig.getStockLocation().getLocation(), toBeAssignedStockLocation.getLocation());
        assertEquals(aptitlig.getStockLocation().isAvailable(), toBeAssignedStockLocation.isAvailable());

    }

    @Test
    void shouldReturnExceptionWhenTryingToAssignUnAvailableStockLocationToProductId() {
        Long id = 1001L;
        Long input = 103L;
        when(productRepository.findById(id)).thenReturn(Optional.of(aptitlig));
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
        when(stockLocationRepository.findById(input)).thenReturn(Optional.of(aptitligStocklocation));
        when(stockLocationService.availableStockLocationId(input)).thenReturn(true);
        when(stockLocationRepository.findById(input)).thenReturn(Optional.of(toBeAssignedStockLocation));
        assertThrows(RecordNotFoundException.class, () -> productService.assignStockLocationToProduct(id, input));
    }

    @Test
    void shouldReturnExceptionWhenTryingToAssignNotExistingStockLocationToProductId() {
        Long id = 1001L;
        Long input = null;
        when(productRepository.findById(id)).thenReturn(Optional.of(aptitlig));
        when(stockLocationRepository.findById(input)).thenReturn(null);

        assertThrows(RecordNotFoundException.class, () -> productService.assignStockLocationToProduct(id, input));
    }

    // TODO assignImageToProduct


    @Test
    void shouldReturnProductDtoWhenProductIsGiven() {
        aptitlig.setFile(aptitligImage);
        ProductDto result = productService.toProductDto(aptitlig);

        assertEquals(aptitligDto.getName(), result.getName());
        assertEquals(aptitligDto.getPrice(), result.getPrice());
        assertEquals(aptitligDto.getCategory(), result.getCategory());
        assertEquals(aptitligDto.getDescription(), result.getDescription());
    }

    @Test
    void shouldReturnProductOnOrderDtoWhenProductIsGiven() {
        aptitlig.setFile(aptitligImage);
        aptitlig.setStockLocation(aptitligStocklocation);
        ProductOnOrderDto result = productService.toProductOnOrderDto(aptitlig);

        assertEquals(aptitligDto.getName(), result.getName());
        assertEquals(aptitligDto.getPrice(), result.getPrice());
        assertEquals(aptitligDto.getCategory(), result.getCategory());
    }


    @Test
    void shouldReturnProductWhenProductDtoIsGiven() {
        Product result = productService.fromProductDto(aptitligInputDto);

        assertEquals(aptitligInputDto.getName(), result.getName());
        assertEquals(aptitligInputDto.getPrice(), result.getPrice());
        assertEquals(aptitligInputDto.getCategory(), result.getCategory());
        assertEquals(aptitligInputDto.getDescription(), result.getDescription());

    }


    @Test
    void shouldReturnTrueIfProductIdIsAvailable() {
        Long id = 1001L;
        when(productRepository.findById(id)).thenReturn(Optional.of(aptitlig));

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
package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.StockLocationDto;
import nl.dentro.OrderSystem.dtos.StockLocationInputDto;
import nl.dentro.OrderSystem.exceptions.RecordCanNotBeDeletedException;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.Product;
import nl.dentro.OrderSystem.models.StockLocation;
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
class StockLocationServiceImplTest {

    @Mock
    StockLocationRepository stockLocationRepository;

    @InjectMocks
    private StockLocationServiceImpl stockLocationService;

    @Captor
    ArgumentCaptor<StockLocation> argumentCaptor;

    StockLocation stockLocation1;
    StockLocation stockLocation2;
    StockLocation stockLocation3;

    StockLocationInputDto stockLocationInputDto;

    @BeforeEach
    void setUp() {
        stockLocation1 = new StockLocation(100L, "05.12.2", true);
        stockLocation2 = new StockLocation(101L, "01.10.1", true);
        stockLocation3 = new StockLocation(101L, "03.15.4", false);

        stockLocationInputDto = new StockLocationInputDto("05.12.2");
    }

    @Test
    void shouldReturnAllAvailableStockLocations() {
        when(stockLocationRepository.findByAvailableIsTrue()).thenReturn(List.of(stockLocation1, stockLocation2));

        List<StockLocationDto> foundStockLocationsList = stockLocationService.getAllAvailableStockLocations();

        assertEquals(stockLocation1.getLocation(), foundStockLocationsList.get(0).getLocation());
        assertEquals(stockLocation2.getLocation(), foundStockLocationsList.get(1).getLocation());
    }

    @Test
    void shouldReturnRecordNotFoundExceptionWhenNoStockLocationsAreAvailable() {
        assertThrows(RecordNotFoundException.class, () -> stockLocationService.getAllAvailableStockLocations());
    }

    @Test
    void shouldCreateStockLocationWhenStockLocationDtoIsGiven() {
        when(stockLocationRepository.save(Mockito.any(StockLocation.class))).thenReturn(stockLocation1);

        stockLocationService.createStockLocation(stockLocationInputDto);
        verify(stockLocationRepository, times(1)).save(argumentCaptor.capture());
        StockLocation stockLocation = argumentCaptor.getValue();

        assertEquals(stockLocationInputDto.getLocation(), stockLocation.getLocation());
    }

    @Test
    void shouldDeleteStockLocationWhenIdIsGiven() {
        when(stockLocationRepository.findById(anyLong())).thenReturn(Optional.of(stockLocation1));

        stockLocationService.deleteStockLocation(anyLong());

        verify(stockLocationRepository).deleteById(anyLong());
    }

    @Test
    void shouldReturnRecordCanNotBeDeletedExceptionWhenStockLocationIsInUsedByAProduct() {
        Long id = 101L;
        when(stockLocationRepository.findById(id)).thenReturn(Optional.of(stockLocation3));

        assertThrows(RecordCanNotBeDeletedException.class, () -> stockLocationService.deleteStockLocation(id));
    }

    @Test
    void shouldReturnRecordNotFoundExceptionWhenNotExistingStockLocationIdIsGiven() {
        Long id = null;

        assertThrows(RecordNotFoundException.class, () -> stockLocationService.deleteStockLocation(id));
    }


    @Test
    void shouldSetStockLocationAvailableWhenIdIsGiven() {
        Long id = 103L;
        when(stockLocationRepository.findById(anyLong())).thenReturn(Optional.of(stockLocation3));

        stockLocationService.setStockLocationAvailable(id);
        verify(stockLocationRepository, times(1)).save(argumentCaptor.capture());
        StockLocation stockLocation = argumentCaptor.getValue();

        assertEquals(stockLocation3.isAvailable(), stockLocation.isAvailable());
    }
}
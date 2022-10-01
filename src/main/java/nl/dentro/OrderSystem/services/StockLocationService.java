package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.StockLocationDto;
import nl.dentro.OrderSystem.dtos.StockLocationInputDto;
import nl.dentro.OrderSystem.models.StockLocation;

import java.util.List;

public interface StockLocationService {

    List<StockLocationDto> getAllAvailableStockLocations();

    StockLocationDto createStockLocation(StockLocationInputDto stockLocationInputDto);

    void deleteStockLocation(Long id);

    StockLocation fromStockLocationDto(StockLocationInputDto stockLocationInputDto);

    StockLocationDto toStockLocationDto(StockLocation stockLocation);
}

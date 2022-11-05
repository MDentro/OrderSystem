package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.StockLocationDto;
import nl.dentro.OrderSystem.dtos.StockLocationInputDto;
import nl.dentro.OrderSystem.exceptions.RecordCanNotBeDeletedException;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.StockLocation;
import nl.dentro.OrderSystem.repositories.StockLocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockLocationServiceImpl implements StockLocationService {

    private final StockLocationRepository stockLocationRepository;

    public StockLocationServiceImpl(StockLocationRepository stockLocationRepository) {
        this.stockLocationRepository = stockLocationRepository;
    }

    @Override
    public List<StockLocationDto> getAllAvailableStockLocations() {
        List<StockLocation> stockLocationsList = stockLocationRepository.findByAvailableIsTrue();

        if (stockLocationsList.size() == 0) {
            throw new RecordNotFoundException("No available stock locations, please create a new one.");
        }

        return fromStockLocationListToDtoList(stockLocationsList);
    }

    @Override
    public StockLocationDto createStockLocation(StockLocationInputDto stockLocationInputDto) {
        StockLocation stockLocation = fromStockLocationDto(stockLocationInputDto);
        StockLocation savedStockLocation = stockLocationRepository.save(stockLocation);
        StockLocationDto stockLocationDto = toStockLocationDto(savedStockLocation);
        return stockLocationDto;
    }

    @Override
    public void deleteStockLocation(Long id) {
        if (availableStockLocationId(id)) {
            StockLocation stockLocation = stockLocationRepository.findById(id).get();
            if(stockLocation.isAvailable()) {
                stockLocationRepository.deleteById(id);
            } else {
                throw new RecordCanNotBeDeletedException("Stock location with id: " + id
                        + " is in use by a product and cannot be deleted.");
            }

        } else {
            throw new RecordNotFoundException("Could not find stock location with id: " + id + ".");
        }
    }

    @Override
    public void setStockLocationAvailable(Long id) {
        StockLocation stockLocation = stockLocationRepository.findById(id).get();
        stockLocation.setAvailable(true);
        stockLocationRepository.save(stockLocation);
    }

    @Override
    public StockLocation fromStockLocationDto(StockLocationInputDto stockLocationInputDto) {
        var stockLocation = new StockLocation();
        stockLocation.setLocation(stockLocationInputDto.getLocation());

        return stockLocation;
    }

    @Override
    public StockLocationDto toStockLocationDto(StockLocation stockLocation) {
        var dto = new StockLocationDto();
        dto.setId(stockLocation.getId());
        dto.setLocation(stockLocation.getLocation());

        return dto;
    }

    @Override
    public List<StockLocationDto> fromStockLocationListToDtoList(List<StockLocation> stockLocations) {
        List<StockLocationDto> stockLocationDtoList = new ArrayList<>();
        for (StockLocation stockLocation : stockLocations) {
            StockLocationDto dto = toStockLocationDto(stockLocation);
            stockLocationDtoList.add(dto);
        }
        return stockLocationDtoList;
    }

    @Override
    public boolean availableStockLocationId(Long id) {
        return stockLocationRepository.findById(id).isPresent();
    }
}

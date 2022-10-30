package nl.dentro.OrderSystem.controllers;

import nl.dentro.OrderSystem.dtos.StockLocationDto;
import nl.dentro.OrderSystem.dtos.StockLocationInputDto;
import nl.dentro.OrderSystem.services.StockLocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static nl.dentro.OrderSystem.util.UtilityMethods.getValidationErrorMessage;

@RestController
@RequestMapping("/stocklocations")
public class StockLocationController {

    private final StockLocationService stockLocationService;

    public StockLocationController(StockLocationService stockLocationService) {
        this.stockLocationService = stockLocationService;
    }

    @GetMapping("")
    public ResponseEntity<List<StockLocationDto>> getAllAvailableStockLocations() {
        List<StockLocationDto> dtos = stockLocationService.getAllAvailableStockLocations();
        return ResponseEntity.ok().body(dtos);
    }


    @PostMapping("")
    public ResponseEntity<Object> createStockLocation(@Valid @RequestBody StockLocationInputDto stockLocationInputDto,
                                                      BindingResult br) {

        if (br.hasErrors()) {
            return new ResponseEntity<>(getValidationErrorMessage(br), HttpStatus.BAD_REQUEST);
        } else {
            StockLocationDto stockLocationDto = stockLocationService.createStockLocation(stockLocationInputDto);
            return new ResponseEntity<>(stockLocationDto, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStockLocation(@PathVariable Long id) {
        stockLocationService.deleteStockLocation(id);
        return ResponseEntity.noContent().build();
    }
}

package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.StockLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockLocationRepository extends JpaRepository<StockLocation, Long> {
    List<StockLocation> findByAvailableIsTrue();
}

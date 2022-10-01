package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.Order;
import nl.dentro.OrderSystem.models.StockLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPaidIsFalse();
}

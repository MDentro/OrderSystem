package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPaidIsFalse();
}

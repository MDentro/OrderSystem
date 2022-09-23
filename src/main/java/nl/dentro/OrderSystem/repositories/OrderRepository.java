package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

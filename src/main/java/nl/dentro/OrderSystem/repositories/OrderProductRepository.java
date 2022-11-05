package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.OrderProduct;
import nl.dentro.OrderSystem.models.OrderProductKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey> {
}

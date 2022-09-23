package nl.dentro.OrderSystem.repositories;

import nl.dentro.OrderSystem.models.OrderProduct;
import nl.dentro.OrderSystem.models.OrderProductKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey> {
    Collection<OrderProduct> findAllProductsByOrderId(Long orderId);
}

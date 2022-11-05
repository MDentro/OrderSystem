package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.models.Order;

public interface OrderProductService {
    void saveOrderProduct(Order order, Long productId, int quantity);
}

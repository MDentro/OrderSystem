package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.OrderDto;
import nl.dentro.OrderSystem.dtos.OrderInputDto;
import nl.dentro.OrderSystem.models.Order;

import java.util.List;

public interface OrderService {
    OrderDto getOrderById(Long id);

    void createOrder(OrderInputDto orderInputDto);

    Double calculateTotalBalance(List<Long> idList);

    void processPayment(Long id);

    void CreateIdList(List<Long> idList, OrderInputDto orderInputDto);

    void checkExistingProductById(List<Long> idList);

    void saveOrderProduct(List<Long> idList, Order savedOrder);
}

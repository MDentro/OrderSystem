package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.OrderDto;
import nl.dentro.OrderSystem.dtos.OrderInputDto;
import nl.dentro.OrderSystem.dtos.UnpaidOrderDto;
import nl.dentro.OrderSystem.dtos.UserDataInputDto;
import nl.dentro.OrderSystem.models.Order;

import java.util.List;

public interface OrderService {

    List<UnpaidOrderDto> getUnpaidOrders();

    OrderDto getOrderById(Long id);

    void createOrder(OrderInputDto orderInputDto);

    void processPayment(Long id);

    Double calculateTotalBalance(List<Long> idList);

    void CreateIdList(List<Long> idList, OrderInputDto orderInputDto);

    void checkExistingProductById(List<Long> idList);

    void saveOrderProduct(List<Long> idList, Order savedOrder);

    UserDataInputDto createUserDataInputDto(OrderInputDto orderInputDto);

    List<UnpaidOrderDto> fromUnpaidOrderListToUnpaidOrderDtoList(List<Order> unpaidOrderList);

    boolean availableOrderId(Long id);
}

package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.*;
import nl.dentro.OrderSystem.models.Order;
import nl.dentro.OrderSystem.models.ShoppingItemTransport;

import java.util.List;

public interface OrderService {

    List<UnpaidOrderDto> getUnpaidOrders();

    OrderDto getOrderById(Long id);

    void createOrder(OrderInputDto orderInputDto);

    void processPayment(Long id);

    Double calculateTotalBalance(List<ShoppingItemTransportInputDto> shoppingItemInputDtos);

    void createIdList(List<Long> idList, List<ShoppingItemTransport> shoppingItemList);

    void checkExistingProductById(List<Long> idList);

    UserDataInputDto createUserDataInputDto(OrderInputDto orderInputDto);

    List<UnpaidOrderDto> fromUnpaidOrderListToUnpaidOrderDtoList(List<Order> unpaidOrderList);

    List<ShoppingItemTransport> fromShoppingItemInputDtoList(List<ShoppingItemTransportInputDto> shoppingItemInputDto);

    ShoppingItemTransport fromShoppingItemDto(ShoppingItemTransportInputDto shoppingItemInputDto);

    boolean availableOrderId(Long id);
}

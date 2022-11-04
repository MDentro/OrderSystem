package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.*;
import nl.dentro.OrderSystem.exceptions.DuplicateFoundException;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.exceptions.UnpaidOrderNotFoundException;
import nl.dentro.OrderSystem.models.*;
import nl.dentro.OrderSystem.repositories.OrderRepository;
import nl.dentro.OrderSystem.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserDataService userDataService;

    private final ProductService productService;

    private final ProductRepository productRepository;

    private final OrderProductService orderProductService;

    public OrderServiceImpl(OrderRepository orderRepository, UserDataService userDataService,
                            ProductService productService, ProductRepository productRepository,
                            OrderProductService orderProductService) {

        this.orderRepository = orderRepository;
        this.userDataService = userDataService;
        this.productService = productService;
        this.productRepository = productRepository;
        this.orderProductService = orderProductService;
    }

    @Override
    public List<UnpaidOrderDto> getUnpaidOrders() {
        List<Order> unpaidOrderList = orderRepository.findByPaidIsFalse();
        if (unpaidOrderList.size() == 0) {
            throw new RecordNotFoundException("Could not find any unpaid orders.");
        }

        return fromUnpaidOrderListToUnpaidOrderDtoList(unpaidOrderList);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        if (!availableOrderId(id)) {
            throw new RecordNotFoundException("Could not find order with id " + id + ".");
        }

        OrderDto orderDto = new OrderDto();
        Collection productDtoList = orderDto.getProductsDtoCollection();
        if (availableOrderId(id)) {
            Order order = orderRepository.findById(id).get();
            Collection<OrderProduct> productList = order.getOrderProduct();
            for (OrderProduct orderProduct : productList) {
                productDtoList.add(productService.toProductOnOrderDto(orderProduct.getProduct(), orderProduct.getQuantity()));
            }
            orderDto.setId(order.getId());
            orderDto.setUserDataDto(userDataService.toUserDataDto(order.getUserData()));
            orderDto.setPaid(order.isPaid());
            orderDto.setTotalPrice(order.getTotalPrice());
        }
        return orderDto;
    }

    @Override
    public void createOrder(OrderInputDto orderInputDto) {
        List<ShoppingItemTransport> shoppingItemList = fromShoppingItemInputDtoList(orderInputDto.getShoppingItemTransportInputDto());

        List<Long> idList = new ArrayList<>();
        createIdList(idList, shoppingItemList);
        checkExistingProductById(idList);

        UserData userData = userDataService.createUserData(createUserDataInputDto(orderInputDto));
        Order order = new Order(calculateTotalBalance(orderInputDto.getShoppingItemTransportInputDto()), false, userData);
        Order savedOrder = orderRepository.save(order);

        for (ShoppingItemTransport item : shoppingItemList) {
            orderProductService.saveOrderProduct(savedOrder, item.getProductId(), item.getQuantity());
        }
    }

    @Override
    public void processPayment(Long id) {
        if (!availableOrderId(id)) {
            throw new RecordNotFoundException("Could not find order with id: " + id + ".");
        }
        Order order = orderRepository.findById(id).get();
        if (order.isPaid()) {
            throw new UnpaidOrderNotFoundException("The order with id: " + id + " is already paid.");
        } else {
            order.setPaid(true);
            orderRepository.save(order);
        }
    }

    @Override
    public Double calculateTotalBalance(List<ShoppingItemTransportInputDto> shoppingItemInputDtos) {
        Double totalBalance = 0.0;
        for (ShoppingItemTransport shoppingItem : fromShoppingItemInputDtoList(shoppingItemInputDtos)) {
            Product product = productRepository.findById(shoppingItem.getProductId()).get();
            int quantity = shoppingItem.getQuantity();

            totalBalance += product.getPrice() * quantity;
        }
        return Math.round(totalBalance * 100.0) / 100.0;
    }

    @Override
    public void createIdList(List<Long> idList, List<ShoppingItemTransport> shoppingItemList) {
        for (ShoppingItemTransport item : shoppingItemList) {
            if (idList.contains(item.getProductId())) {
                throw new DuplicateFoundException("Duplicate order id found: " + item.getProductId() + ".");
            } else {
                idList.add(item.getProductId());
            }
        }
    }

    @Override
    public void checkExistingProductById(List<Long> idList) {
        for (Long id : idList) {
            if (!productService.availableProductId(id)) {
                throw new RecordNotFoundException("Could not find product with id: " + id + ".");
            }
        }
    }

    @Override
    public UserDataInputDto createUserDataInputDto(OrderInputDto orderInputDto) {
        return new UserDataInputDto(orderInputDto.getFirstName(), orderInputDto.getLastName(),
                orderInputDto.getEmail(), orderInputDto.getPhoneNumber());
    }

    @Override
    public List<UnpaidOrderDto> fromUnpaidOrderListToUnpaidOrderDtoList(List<Order> unpaidOrderList) {
        List<UnpaidOrderDto> unpaidOrderDtoList = new ArrayList<>();
        for (Order order : unpaidOrderList) {
            UnpaidOrderDto dto = new UnpaidOrderDto(order.getId(), order.getTotalPrice(),
                    order.getUserData().getFirstName(), order.getUserData().getLastName());
            unpaidOrderDtoList.add(dto);
        }
        return unpaidOrderDtoList;
    }

    @Override
    public List<ShoppingItemTransport> fromShoppingItemInputDtoList(List<ShoppingItemTransportInputDto> shoppingItemInputDto) {
        List<ShoppingItemTransport> shoppingItemList = new ArrayList<>();
        for (ShoppingItemTransportInputDto dto : shoppingItemInputDto) {
            shoppingItemList.add(fromShoppingItemDto(dto));
        }
        return shoppingItemList;
    }

    @Override
    public ShoppingItemTransport fromShoppingItemDto(ShoppingItemTransportInputDto shoppingItemInputDto) {
        var shoppingItem = new ShoppingItemTransport();
        shoppingItem.setProductId(shoppingItemInputDto.getProductId());
        shoppingItem.setQuantity(shoppingItemInputDto.getQuantity());

        return shoppingItem;
    }

    @Override
    public boolean availableOrderId(Long id) {
        return orderRepository.findById(id).isPresent();
    }
}

package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.OrderDto;
import nl.dentro.OrderSystem.dtos.OrderInputDto;
import nl.dentro.OrderSystem.dtos.UserInputDto;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.exceptions.UnpaidOrderNotFoundException;
import nl.dentro.OrderSystem.models.Order;
import nl.dentro.OrderSystem.models.OrderProduct;
import nl.dentro.OrderSystem.models.Product;
import nl.dentro.OrderSystem.models.User;
import nl.dentro.OrderSystem.repositories.OrderProductRepository;
import nl.dentro.OrderSystem.repositories.OrderRepository;
import nl.dentro.OrderSystem.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    private final ProductService productService;

    private final ProductRepository productRepository;

    private final OrderProductService orderProductService;

    private final OrderProductRepository orderProductRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, ProductService productService, ProductRepository productRepository, OrderProductService orderProductService, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
        this.productRepository = productRepository;
        this.orderProductService = orderProductService;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public OrderDto getOrderById(Long id) {
        if(!orderRepository.findById(id).isPresent()) {
            throw new RecordNotFoundException("Could not find order with id " + id + ".");
        }

        OrderDto orderDto = new OrderDto();
        Collection productDtoList = orderDto.getProductsDtoCollection();
        if (orderRepository.findById(id).isPresent()) {
            Order order = orderRepository.findById(id).get();
            Collection<OrderProduct> productList = orderProductRepository.findAllProductsByOrderId(id);
            for (OrderProduct orderProduct : productList) {
                productDtoList.add(productService.toProductDto(orderProduct.getProduct()));
            }
            orderDto.setId(order.getId());
            orderDto.setUserDto(userService.toUserDto(order.getUser()));
            orderDto.setPaid(order.isPaid());
            orderDto.setTotalPrice(order.getTotalPrice());
        }
        return orderDto;
    }


    @Override
    public void createOrder(OrderInputDto orderInputDto) {
        List<Long> idList = new ArrayList<>();
        CreateIdList(idList, orderInputDto);
        checkExistingProductById(idList);
        User user = userService.createUser(createUserInputDto(orderInputDto));
        Order order = new Order(calculateTotalBalance(idList), false, user);
        Order savedOrder = orderRepository.save(order);
        saveOrderProduct(idList, savedOrder);
    }

    @Override
    public Double calculateTotalBalance(List<Long> idList) {
        Double totalBalance = 0.0;
        for (Long id : idList) {
            Product product = productRepository.findById(id).get();
            totalBalance = totalBalance + product.getPrice();
        }
        return totalBalance;
    }

    @Override
    public void processPayment(Long id) {
        if (!availableOrderId(id)) {
            throw new RecordNotFoundException("Could not find order with id: " + id + ".");
        }
        Order order = orderRepository.findById(id).get();
        if (order.isPaid()) {
            throw new UnpaidOrderNotFoundException("The order with id:" + id + " is already paid.");
        } else {
            order.setPaid(true);
            orderRepository.save(order);
        }
    }

    @Override
    public void CreateIdList(List<Long> idList, OrderInputDto orderInputDto) {
        for (Long id : orderInputDto.getProductIds()) {
            idList.add(id);
        }
    }

    @Override
    public void checkExistingProductById(List<Long> idList) {
        for (Long id : idList) {
            if (!productRepository.findById(id).isPresent()) {
                throw new RecordNotFoundException("Could not find product with id: " + id + ".");
            }
        }
    }

    @Override
    public void saveOrderProduct(List<Long> idList, Order savedOrder) {
        for (Long id : idList) {
            orderProductService.saveOrderProduct(savedOrder, id);
        }
    }


    public UserInputDto createUserInputDto(OrderInputDto orderInputDto) {
        return new UserInputDto(orderInputDto.getFirstName(), orderInputDto.getLastName(), orderInputDto.getEmail(), orderInputDto.getPhoneNumber());
    }

    public boolean availableOrderId(Long id) {
        return orderRepository.findById(id).isPresent();
    }

}

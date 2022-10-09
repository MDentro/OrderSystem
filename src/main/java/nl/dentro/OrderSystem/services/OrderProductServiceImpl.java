package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.Order;
import nl.dentro.OrderSystem.models.OrderProduct;
import nl.dentro.OrderSystem.models.OrderProductKey;
import nl.dentro.OrderSystem.models.Product;
import nl.dentro.OrderSystem.repositories.OrderProductRepository;
import nl.dentro.OrderSystem.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderProductServiceImpl implements OrderProductService {
    private final OrderProductRepository orderProductRepository;

    private final ProductRepository productRepository;

    public OrderProductServiceImpl(OrderProductRepository orderProductRepository, ProductRepository productRepository) {
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void saveOrderProduct(Order order, Long productId) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        if (!productRepository.findById(productId).isPresent()) {
            throw new RecordNotFoundException("Could not find product with id: " + productId + ".");
        }
        Product product = productRepository.findById(productId).get();
        orderProduct.setProduct(product);
        OrderProductKey id = new OrderProductKey(order.getId(), product.getId());
        orderProduct.setId(id);
        orderProductRepository.save(orderProduct);
    }

    @Override
    public boolean isProductOrdered(Long id) {
        if (orderProductRepository.findAllOrdersByProductId(id).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}

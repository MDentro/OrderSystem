package nl.dentro.OrderSystem.controllers;

import nl.dentro.OrderSystem.services.OrderProductService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderProductController {
    private final OrderProductService orderProductService;

    public OrderProductController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }
}

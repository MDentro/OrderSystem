package nl.dentro.OrderSystem.controllers;

import nl.dentro.OrderSystem.dtos.OrderDto;
import nl.dentro.OrderSystem.dtos.OrderInputDto;
import nl.dentro.OrderSystem.dtos.UnpaidOrderDto;
import nl.dentro.OrderSystem.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static nl.dentro.OrderSystem.util.UtilityMethods.getValidationErrorMessage;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<List<UnpaidOrderDto>> getUnpaidOrders() {
        List<UnpaidOrderDto> dtos;
        dtos = orderService.getUnpaidOrders();
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
        OrderDto orderDto = orderService.getOrderById(id);
        return ResponseEntity.ok().body(orderDto);
    }

    @PostMapping("")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderInputDto orderInputDto,
                                              BindingResult br) {

        if (br.hasErrors()) {
            return new ResponseEntity<>(getValidationErrorMessage(br), HttpStatus.BAD_REQUEST);
        } else {
            orderService.createOrder(orderInputDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> processPayment(@PathVariable Long id) {
        orderService.processPayment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

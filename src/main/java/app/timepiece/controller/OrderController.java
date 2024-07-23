package app.timepiece.controller;

import app.timepiece.dto.OrderDTO;
import app.timepiece.dto.UserOrderDTO;
import app.timepiece.entity.Order;
import app.timepiece.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(
            @RequestParam Long watchId,
            @RequestParam Long userId)

    {

        orderService.createOrder(watchId, userId);
        return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<UserOrderDTO> getOrderById(@PathVariable Long orderId) {
        UserOrderDTO order = orderService.getOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/buyer/{userId}")
    public ResponseEntity<List<UserOrderDTO>> getOrdersByBuyerId(@PathVariable Long userId) {
        List<UserOrderDTO> orders = orderService.getOrdersByBuyerId(userId);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<List<UserOrderDTO>> getOrdersBySellerId(@PathVariable Long userId) {
        List<UserOrderDTO> orders = orderService.getOrdersBySellerId(userId);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<UserOrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        try {
            UserOrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{orderId}/buyeraddress")
    public ResponseEntity<?> updateBuyerAddress(@PathVariable Long orderId, @RequestParam String newAddress,@RequestParam(required = false) String paymentMethod) {
        try {
            UserOrderDTO updatedOrder = orderService.updateBuyerAddress(orderId, newAddress, paymentMethod);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long orderId) {
        return orderService.completeOrder(orderId);
    }
}

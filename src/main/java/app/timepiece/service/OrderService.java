package app.timepiece.service;

import app.timepiece.dto.UserOrderDTO;
import app.timepiece.entity.Order;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    Order createOrder(Long watchId, Long userId, String paymentMethod);
    UserOrderDTO getOrderById(Long orderId);
    List<UserOrderDTO> getOrdersByBuyerId(Long buyerId);
    List<UserOrderDTO> getOrdersBySellerId(Long sellerId);
    UserOrderDTO updateOrderStatus(Long orderId, String status);
    UserOrderDTO updateBuyerAddress(Long orderId, String newAddress);
    ResponseEntity<String> completeOrder(Long orderId);
}

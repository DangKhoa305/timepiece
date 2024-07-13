package app.timepiece.service;

import app.timepiece.dto.OrderDTO;
import app.timepiece.dto.UserOrderDTO;
import app.timepiece.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Long watchId, Long userId);
    OrderDTO getOrderById(Long orderId);
    List<UserOrderDTO> getOrdersByBuyerId(Long buyerId);
    List<UserOrderDTO> getOrdersBySellerId(Long sellerId);
    OrderDTO updateOrderStatus(Long orderId, String status);
}

package app.timepiece.service.serviceImpl;


import app.timepiece.dto.OrderDTO;
import app.timepiece.dto.UserOrderDTO;
import app.timepiece.dto.WatchDTO;
import app.timepiece.entity.Order;
import app.timepiece.entity.User;
import app.timepiece.entity.Watch;
import app.timepiece.entity.WatchImage;
import app.timepiece.repository.OrderRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.repository.WatchRepository;
import app.timepiece.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.WatchService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WatchRepository watchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchServiceImpl watchService;

    @Override
    public Order createOrder(Long watchId, Long userId) {
        // Lấy thông tin Watch từ WatchService dựa trên watchId
        Optional<Watch> watchOptional = watchRepository.findById(watchId);
        if (watchOptional.isEmpty()) {
            throw new IllegalArgumentException("Watch not found with id: " + watchId);
        }
        Watch watch = watchOptional.get();

        // Lấy thông tin User từ UserRepository dựa trên userId
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        User user = userOptional.get();

        // Tạo đối tượng Order
        Order order = Order.builder()
                .orderDate(new Date()) // Có thể sử dụng thời gian từ request nếu có
                .totalPrice(watch.getPrice())
                .createDate(new Date())
                .updateDate(new Date())
                .status("wait")
                .user(user)
                .watch(watch)
                .build();

        // Lưu Order vào cơ sở dữ liệu và trả về đối tượng đã lưu
        return orderRepository.save(order);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            return null;
        }
        Order order = orderOptional.get();
        WatchDTO watch = watchService.getWatchById(order.getWatch().getId());

        return convertToOrderDTO(order,watch.getUserId());
    }

    private OrderDTO convertToOrderDTO(Order order , Long sellerId ) {
        List<String> watchImages = order.getWatch().getImages().stream()
                .map(WatchImage::getImageUrl)
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .userId(order.getUser().getId())
                .watchName(order.getWatch().getName())
                .status(order.getStatus())
                .watchImages(watchImages)
                .sellerId(sellerId)
                .build();
    }

    @Override
    public List<UserOrderDTO> getOrdersByBuyerId(Long buyerId) {
        List<Order> orders = orderRepository.findByUserId(buyerId);
        return orders.stream()
                .map(this::convertToUserOrderDTO)
                .collect(Collectors.toList());
    }

    public List<UserOrderDTO> getOrdersBySellerId(Long sellerId) {
        List<Order> orders = orderRepository.findOrdersBySellerId(sellerId);
        return orders.stream()
                .map(this::convertToUserOrderDTO)
                .collect(Collectors.toList());

    }

    private UserOrderDTO convertToUserOrderDTO(Order order) {
        List<String> watchImages = order.getWatch().getImages().stream()
                .map(WatchImage::getImageUrl)
                .collect(Collectors.toList());

        return UserOrderDTO.builder()
                .id(order.getId())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .watchName(order.getWatch().getName())
                .watchImages(watchImages)
                .build();
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new RuntimeException("Order not found");
        }
        Order order = orderOptional.get();
        order.setStatus(status);
        order.setUpdateDate(new Date());
        Order updatedOrder = orderRepository.save(order);
        return getOrderById(updatedOrder.getId());
    }
}

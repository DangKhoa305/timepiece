package app.timepiece.service.serviceImpl;


import app.timepiece.dto.*;
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



    @Override
    public Order createOrder(Long watchId, Long userId) {

        Optional<Watch> watchOptional = watchRepository.findById(watchId);
        if (watchOptional.isEmpty()) {
            throw new IllegalArgumentException("Watch not found with id: " + watchId);
        }
        Watch watch = watchOptional.get();

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        User user = userOptional.get();


        Order order = Order.builder()
                .orderDate(new Date())
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
    public UserOrderDTO getOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            return null;
        }
        Order order = orderOptional.get();
        return convertToUserOrderDTO(order);
    }

//    private OrderDTO convertToOrderDTO(Order order , Long sellerId ) {
//        List<String> watchImages = order.getWatch().getImages().stream()
//                .map(WatchImage::getImageUrl)
//                .collect(Collectors.toList());
//
//        return OrderDTO.builder()
//                .id(order.getId())
//                .note(order.getNote())
//                .orderDate(order.getOrderDate())
//                .totalPrice(order.getTotalPrice())
//                .userId(order.getUser().getId())
//                .watchName(order.getWatch().getName())
//                .status(order.getStatus())
//                .watchImages(watchImages)
//                .sellerId(sellerId)
//                .build();
//    }

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

        WatchSellerDTO watchSellerDTO = WatchSellerDTO.builder()
                .imageUrl(watchImages.isEmpty() ? null : watchImages.get(0)) // Chọn hình ảnh đầu tiên hoặc null nếu không có hình ảnh
                .name(order.getWatch().getName())
                .size(order.getWatch().getSize())
                .price(order.getWatch().getPrice())
                .createDate(order.getWatch().getCreateDate())
                .address(order.getWatch().getAddress())
                .area((order.getWatch().getArea()))
                .status(order.getWatch().getStatus())
                .type(order.getWatch().getWatchType().getTypeName())
                .brand(order.getWatch().getBrand().getBrandName())
                .build();

        UserDTO sellerDTO = UserDTO.builder()
                .id(order.getWatch().getUser().getId())
                .name(order.getWatch().getUser().getName())
                .address(order.getWatch().getUser().getAddress())
                .avatar(order.getWatch().getUser().getAvatar())
                .phoneNumber(order.getWatch().getUser().getPhoneNumber())
                .status(order.getWatch().getUser().getStatus())
                .dateCreate(order.getWatch().getUser().getDateCreate())
                .gender(order.getWatch().getUser().getGender())
                .birthday(order.getWatch().getUser().getBirthday())
                .citizenID(order.getWatch().getUser().getCitizenID())
                .build();

        UserDTO buyerDTO = UserDTO.builder()
                .id(order.getUser().getId())
                .name(order.getUser().getName())
                .address(order.getUser().getAddress())
                .avatar(order.getUser().getAvatar())
                .phoneNumber(order.getUser().getPhoneNumber())
                .status(order.getUser().getStatus())
                .dateCreate(order.getUser().getDateCreate())
                .gender(order.getUser().getGender())
                .birthday(order.getUser().getBirthday())
                .citizenID(order.getUser().getCitizenID())
                .build();

        return UserOrderDTO.builder()
                .id(order.getId())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .watch(watchSellerDTO)
                .seller(sellerDTO)
                .buyer(buyerDTO)
                .build();
    }

    @Override
    public UserOrderDTO updateOrderStatus(Long orderId, String status) {
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

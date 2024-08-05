package app.timepiece.service.serviceImpl;


import app.timepiece.dto.NotificationDTO;
import app.timepiece.entity.Notification;
import app.timepiece.entity.User;
import app.timepiece.repository.NotificationRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Notification createNotification(Long userId, String message) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Notification notification = Notification.builder()
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public NotificationDTO convertToDTO(Notification notification) {
        User user = notification.getUser();
        return NotificationDTO.builder()
                .id(notification.getId())
                .userId(user.getId())
                .userName(user.getName())
                .avatar(user.getAvatar())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}


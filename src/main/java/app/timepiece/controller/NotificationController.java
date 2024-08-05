package app.timepiece.controller;

import app.timepiece.dto.NotificationDTO;
import app.timepiece.entity.Notification;
import app.timepiece.service.NotificationService;
import app.timepiece.service.serviceImpl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/create")
    public Notification createNotification(@RequestParam Long userId, @RequestParam String message) {
        return notificationService.createNotification(userId, message);
    }

    @GetMapping("/user/{userId}")
    public List<NotificationDTO> getNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return notifications.stream()
                .map(notification -> ((NotificationServiceImpl) notificationService).convertToDTO(notification))
                .collect(Collectors.toList());
    }
}

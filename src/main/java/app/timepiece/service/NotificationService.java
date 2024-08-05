package app.timepiece.service;

import app.timepiece.entity.Notification;
import java.util.List;

public interface NotificationService {
    Notification createNotification(Long userId, String message);
    List<Notification> getNotificationsByUserId(Long userId);
}


package org.example.medisched.service;

import org.example.medisched.entity.Notifications;
import org.example.medisched.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NotificationsService {
    List<Notifications> getAllNotifications();
    List<Notifications> getNotificationsByUserId(Long userId);
    Notifications createNotification(Notifications notification);
    void markAsRead(Long id);
    void deleteNotification(Long id);

    @Service
    class NotificationsServiceImpl implements NotificationsService {

        @Autowired
        private NotificationsRepository notificationRepository;

        @Override
        public List<Notifications> getAllNotifications() {
            return notificationRepository.findAll();
        }

        @Override
        public List<Notifications> getNotificationsByUserId(Long userId) {
            return notificationRepository.findByUserId(userId);
        }

        @Override
        public Notifications createNotification(Notifications notification) {
            return notificationRepository.save(notification);
        }

        @Override
        public void markAsRead(Long id) {
            notificationRepository.findById(id).ifPresent(notification -> {
                notification.setRead(true);
                notificationRepository.save(notification);
            });
        }

        @Override
        public void deleteNotification(Long id) {
            notificationRepository.deleteById(id);
        }
    }
}

package com.example.civic_fix.Repository;

 
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.example.civic_fix.Entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_UserId(Long userId);
    List<Notification> findByUser_UserIdAndIsReadFalse(Long userId);
    List<Notification> findAll(Sort sort);  
}
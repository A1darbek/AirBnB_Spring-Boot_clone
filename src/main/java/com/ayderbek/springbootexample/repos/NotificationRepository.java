package com.ayderbek.springbootexample.repos;

import com.ayderbek.springbootexample.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}

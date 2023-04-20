package com.ayderbek.springbootexample.websocket;

import com.ayderbek.springbootexample.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestamp(
            Integer senderId, Integer receiverId, Integer receiverId2, Integer senderId2
    );
}

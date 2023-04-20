package com.ayderbek.springbootexample.websocket;

import com.ayderbek.springbootexample.domain.Message;
import com.ayderbek.springbootexample.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;


    public void createMessage(Integer senderId, Integer receiverId, String content, LocalDateTime timestamp) {
        User sender = new User();
        sender.setId(senderId);

        User receiver = new User();
        receiver.setId(receiverId);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(timestamp);

        messageRepository.save(message);
    }

    public List<Message> getMessages(Integer senderId, Integer receiverId) {
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestamp(senderId, receiverId, senderId, receiverId);
    }
}

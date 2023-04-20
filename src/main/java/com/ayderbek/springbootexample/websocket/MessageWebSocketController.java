package com.ayderbek.springbootexample.websocket;

import com.ayderbek.springbootexample.domain.Message;
import com.ayderbek.springbootexample.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    private final MessageService messageService;


    @MessageMapping("/chat")
    public void sendMessage(MessageDTO messageDto) {
        messageService.createMessage(messageDto.getSenderId(), messageDto.getReceiverId(), messageDto.getContent(), messageDto.getTimestamp());
        messagingTemplate.convertAndSendToUser(String.valueOf(messageDto.getReceiverId()), "/topic/chat", messageDto);
    }

    @SubscribeMapping("/chat/{senderId}/{receiverId}")
    public List<MessageDTO> getMessages(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
        List<Message> messages = messageService.getMessages(senderId, receiverId);
        List<MessageDTO> messageDtos = messages.stream()
                .map(m -> new MessageDTO(m.getSender().getId(), m.getReceiver().getId(), m.getContent(), m.getTimestamp()))
                .collect(Collectors.toList());
        return messageDtos;
    }
}

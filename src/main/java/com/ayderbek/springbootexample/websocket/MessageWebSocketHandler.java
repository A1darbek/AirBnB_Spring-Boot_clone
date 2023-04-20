package com.ayderbek.springbootexample.websocket;

import com.ayderbek.springbootexample.domain.dto.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class MessageWebSocketHandler extends TextWebSocketHandler {
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MessageDTO messageDTO = objectMapper.readValue(message.getPayload(), MessageDTO.class);
        messageService.createMessage(messageDTO.getSenderId(), messageDTO.getReceiverId(), messageDTO.getContent(), messageDTO.getTimestamp());
        session.sendMessage(new TextMessage("Message sent successfully"));
    }
}

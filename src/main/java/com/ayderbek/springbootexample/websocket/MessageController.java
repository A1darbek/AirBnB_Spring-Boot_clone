package com.ayderbek.springbootexample.websocket;

import com.ayderbek.springbootexample.domain.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/messages")
public class MessageController {
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Void> createMessage(@RequestBody MessageDTO messageDTO) {
        messageService.createMessage(messageDTO.getSenderId(), messageDTO.getReceiverId(), messageDTO.getContent(), messageDTO.getTimestamp());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

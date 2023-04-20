package com.ayderbek.springbootexample.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private LocalDateTime timestamp;
}

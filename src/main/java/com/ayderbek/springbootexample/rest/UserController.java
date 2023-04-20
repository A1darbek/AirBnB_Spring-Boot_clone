package com.ayderbek.springbootexample.rest;

import com.ayderbek.springbootexample.domain.User;

import com.ayderbek.springbootexample.domain.dto.UserDTO;
import com.ayderbek.springbootexample.ports.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @GetMapping
    public List<UserDTO> getAllUsers() {
        var users  = userService.getAllUsers();
        return users;
    }
}

package com.ayderbek.springbootexample.ports;

import com.amazonaws.services.alexaforbusiness.model.NotFoundException;
import com.ayderbek.springbootexample.domain.User;
import com.ayderbek.springbootexample.domain.dto.UserDTO;
import com.ayderbek.springbootexample.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    public User getUserById(Integer id) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return modelMapper.map(user, User.class);
        } else {
            throw new NotFoundException("User not found with id: " + id);
        }
    }
    public List<UserDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }
//
//    @Transactional
//    public User getUserWithTokens(Integer userId) {
//        User user = repository.findById(userId).orElseThrow();
//        // Accessing the tokens collection within a transactional context
//        List<Token> tokens = user.getTokens();
//        log.info("Tokens fetched for user with id {}: {}", userId, tokens);
//        return user;
//    }
}

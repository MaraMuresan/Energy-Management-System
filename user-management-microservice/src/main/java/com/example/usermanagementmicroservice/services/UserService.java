package com.example.usermanagementmicroservice.services;

import com.example.usermanagementmicroservice.controllers.handlers.exceptions.model.DuplicateResourceException;
import com.example.usermanagementmicroservice.rabbitmq.UserPublisher;
import com.example.usermanagementmicroservice.rabbitmq.UserSynchronizationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.usermanagementmicroservice.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.usermanagementmicroservice.dtos.UserDTO;
import com.example.usermanagementmicroservice.dtos.UserDetailsDTO;
import com.example.usermanagementmicroservice.dtos.builders.UserBuilder;
import com.example.usermanagementmicroservice.entities.User;
import  com.example.usermanagementmicroservice.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    private final UserPublisher userPublisher;

    @Autowired
    public UserService(UserRepository userRepository, UserPublisher userPublisher) {
        this.userRepository = userRepository;
        this.userPublisher = userPublisher;
    }

    public List<UserDTO> findUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return UserBuilder.toUserDTO(userOptional.get());
    }

    @Transactional
    public UUID insert(UserDetailsDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            LOGGER.error("User with username {} already exists", userDTO.getUsername());
            throw new DuplicateResourceException(
                    "A user with this username already exists: " + userDTO.getUsername()
            );
        }

        User user = UserBuilder.toEntity(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        // syncUserReplica(user); this is for assignment 1, rest calls

        // ASSIGNMENT 2 - PUBLISH TO QUEUE
        UserSynchronizationMessage message = new UserSynchronizationMessage();
        message.setEvent("USER_CREATED");
        message.setId(user.getId());
        message.setUsername(user.getUsername());

        System.out.println(">>> CALLING USER PUBLISHER WITH EVENT " + message.getEvent());
        userPublisher.publish(message);
        userPublisher.publish(message);

        return user.getId();
    }

    @Transactional
    public UserDTO update(UUID id, UserDetailsDTO userDTO) {
        Optional<User> userBefore = userRepository.findById(id);
        if (!userBefore.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        if (userRepository.existsByUsername(userDTO.getUsername()) &&
                !userBefore.get().getUsername().equals(userDTO.getUsername())) {
            LOGGER.error("Username {} is already taken by another user", userDTO.getUsername());
            throw new DuplicateResourceException(
                    "A user with this username already exists: " + userDTO.getUsername()
            );
        }

        userBefore.get().setUsername(userDTO.getUsername());
        userBefore.get().setAddress(userDTO.getAddress());
        userBefore.get().setAge(userDTO.getAge());

        User userAfter = userRepository.save(userBefore.get());
        LOGGER.debug("User with id {} was updated in db", userAfter.getId());

        // syncUserReplica(userAfter); this is for assignment 1, rest calls

        // ASSIGNMENT 2 - PUBLISH TO QUEUE
        UserSynchronizationMessage message = new UserSynchronizationMessage();
        message.setEvent("USER_UPDATED");
        message.setId(userAfter.getId());
        message.setUsername(userAfter.getUsername());
        userPublisher.publish(message);

        return UserBuilder.toUserDTO(userAfter);
    }

    //@Transactional in case that device management service fail
    @Transactional
    public void delete(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        userRepository.delete(user.get());
        LOGGER.debug("User with id {} was deleted from db", user.get().getId());

        //rest send request to device
        //String url = "http://device_spring:8082/internalUser/" + id;
        //String url = "http://traefik/device/internalUser/" + id;
        //RestTemplate restTemplate = new RestTemplate();

        /*try {
            restTemplate.delete(url);
            LOGGER.debug("User replica deleted successfully for id {}", id);
        } catch (Exception e) {
            LOGGER.error("Failed to delete user replica from Device service for id {}. Reason: {}", id, e.getMessage());
        }*/

        // ASSIGNMENT 2 - PUBLISH TO QUEUE
        UserSynchronizationMessage message = new UserSynchronizationMessage();
        message.setEvent("USER_DELETED");
        message.setId(id);
        userPublisher.publish(message);
    }


    /*private void syncUserReplica(User user) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> payload = Map.of(
                "id", user.getId(),
                "username", user.getUsername()
        );

        try {
            //restTemplate.postForObject("http://device_spring:8082/internalUser", payload, Void.class);
            restTemplate.postForObject("http://traefik/device/internalUser", payload, Void.class);
            LOGGER.debug("Synced user replica for id {}", user.getId());
        } catch (Exception e) {
            LOGGER.error("Failed to sync user replica: {}", e.getMessage());
        }
    }*/
}

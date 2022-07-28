package com.cubicfox.restApiTest.controller;

import com.cubicfox.restApiTest.client.UserClient;
import com.cubicfox.restApiTest.model.ResponseDTO;
import com.cubicfox.restApiTest.model.Users;
import com.cubicfox.restApiTest.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController()
@RequestMapping("user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserClient userClient;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(@Qualifier("com.cubicfox.restApiTest.client.UserClient") UserClient usersClient, UserServiceImpl userServiceImpl) {
        this.userClient = usersClient;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        List<Users> users;
        try {
            users = userClient.getUsers();
            if (Objects.isNull(users) || users.isEmpty()) {
                logger.warn("User client returned no result");
                return new ResponseEntity<>("User client returned no result", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(users);
    }

    @PutMapping("saveAll")
    public ResponseEntity<Object> saveAll() {
        List<Users> users = userClient.getUsers();
        if (Objects.isNull(users) || users.isEmpty()) {
            logger.warn("User client returned no result");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userServiceImpl.saveAll(users, new ResponseDTO()));
    }


}

package com.cubicfox.restApiTest.controller;

import com.cubicfox.restApiTest.service.UserService;
import com.cubicfox.restApiTest.client.UserClient;
import com.cubicfox.restApiTest.model.SaveResponseDTO;
import com.cubicfox.restApiTest.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    private final UserClient userClient;
    private final UserService userService;

    @Autowired
    public UserController(UserClient usersClient, UserService userService) {
        this.userClient = usersClient;
        this.userService = userService;
    }

    @GetMapping
    public List<Users> getUsers() {
        return userClient.getUsers();
    }

    @PutMapping("saveAll")
    public ResponseEntity<SaveResponseDTO> saveAll() {
        List<Users> users = userClient.getUsers();
        if (Objects.isNull(users) || users.isEmpty()) {
           // log();
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(userService.saveAll(users));
    }

}

package com.cubicfox.restApiTest.client;

import com.cubicfox.restApiTest.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserClientFallBack implements UserClient {
    Logger logger = LoggerFactory.getLogger(UserClientFallBack.class);

    @Override
    public List<Users> getUsers() {
        logger.error("Endpoint cant reach");
        throw new RuntimeException("Endpoint cant reach");
    }

    @Override
    public ResponseEntity<String> getStatus() {
        logger.error("Endpoint cant reach");
        return ResponseEntity.badRequest().body("Endpoint cant reach");
    }
}
package com.cubicfox.restApiTest.client;

import com.cubicfox.restApiTest.model.Users;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
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

@Component
class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {

        String httpStatus = cause instanceof FeignException ? Integer.toString(((FeignException) cause).status()) : "";

        return new UserClient() {
            @Override
            public List<Users> getUsers() {
                //log.error(httpStatus);
                // what you want to answer back (logger, exception catch by a ControllerAdvice, etc)
                throw new RuntimeException(httpStatus);
            }

            @Override
            public ResponseEntity<String> getStatus() {
                throw new RuntimeException(httpStatus);
            }
        };
    }


}
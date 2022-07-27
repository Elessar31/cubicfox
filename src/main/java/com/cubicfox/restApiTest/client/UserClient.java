package com.cubicfox.restApiTest.client;

import com.cubicfox.restApiTest.model.Users;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "userClient", url = "https://jsonplaceholder.typicode.com/")
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET, value = "/users")
    List<Users> getUsers();
}

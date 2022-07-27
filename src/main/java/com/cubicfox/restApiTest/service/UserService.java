package com.cubicfox.restApiTest.service;

import com.cubicfox.restApiTest.model.SaveResponseDTO;
import com.cubicfox.restApiTest.model.Users;
import com.cubicfox.restApiTest.provider.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserProvider userProvider;

    @Autowired
    public UserService(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    public SaveResponseDTO saveAll(List<Users> users) {
        for (Users user: users) {
            userProvider.saveUser(user);
        }
        return new SaveResponseDTO();
    }
}

package com.cubicfox.restApiTest.service;

import com.cubicfox.restApiTest.model.ResponseDTO;
import com.cubicfox.restApiTest.model.SaveResponseDTO;
import com.cubicfox.restApiTest.model.Users;

import java.util.List;

public interface UserService {
    int saveAll(List<Users> users, ResponseDTO responseDTO);
    SaveResponseDTO save(Users users);
}

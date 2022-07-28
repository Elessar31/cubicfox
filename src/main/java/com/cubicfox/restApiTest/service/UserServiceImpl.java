package com.cubicfox.restApiTest.service;

import com.cubicfox.restApiTest.model.ResponseDTO;
import com.cubicfox.restApiTest.model.SaveResponseDTO;
import com.cubicfox.restApiTest.model.Users;
import com.cubicfox.restApiTest.provider.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.cubicfox.restApiTest.Util.EmailValidatorHelper.emailIsValid;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserProvider userProvider;

    @Autowired
    public UserServiceImpl(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    public int saveAll(List<Users> users, ResponseDTO responseDTO) {
        int successCount = 0;
        for (Users user : users) {
            var saveResult = save((user));
            if (saveResult.isSuccess()) {
                successCount++;
            } else {
                responseDTO.getErrorList().add(saveResult.getError());
            }
        }
        responseDTO.setSuccessSaveUser(successCount);
        return successCount;
    }

    public SaveResponseDTO save(Users users) {
        try {
            if (emailIsValid(users.getEmail())) {
                userProvider.saveUser(users);
                logger.info(String.format("User (%s) saved", users.getName()));
            } else {
                logger.info(String.format("Email (%s) is invalid.", users.getEmail()));
                return SaveResponseDTO
                        .builder()
                        .error("Email address is invalid")
                        .success(false)
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Can't save user data", ex);
            return SaveResponseDTO
                    .builder()
                    .error("Can't save user data. "+ ex.getMessage())
                    .success(false)
                    .build();
        }
        return SaveResponseDTO
                .builder()
                .success(true)
                .build();
    }


}

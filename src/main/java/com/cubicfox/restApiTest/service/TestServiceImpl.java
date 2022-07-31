package com.cubicfox.restApiTest.service;

import com.cubicfox.restApiTest.client.UserClient;
import com.cubicfox.restApiTest.model.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TestServiceImpl implements TestService {

    private final UserClient userClient;
    private final UserService userServiceImpl;
    Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    public TestServiceImpl(@Qualifier("com.cubicfox.restApiTest.client.UserClient") UserClient userClient, UserService userServiceImpl) {
        this.userClient = userClient;
        this.userServiceImpl = userServiceImpl;
    }
    @Override
    public int getHttpStatus() {
        try {
            return userClient.getStatus().getStatusCodeValue();
        } catch (Exception ex) {
            logger.error("Ouch, Something wrong happened", ex);
            return HttpStatus.BAD_REQUEST.value();
        }
    }

    @Override
    public ResponseDTO fullTest() {
        ResponseDTO responseDTO = new ResponseDTO();
        logger.info("End point status check starts");
        responseDTO.setStatus(getHttpStatus());

        if (HttpStatus.OK.value() != responseDTO.getStatus()) {
            return responseDTO;
        }
        logger.info("End point consistency check start");
        testReturningConsistency(responseDTO);

        if (HttpStatus.OK.value() != responseDTO.getStatus()) {
            return responseDTO;
        }
        logger.info("Trying to save result");
        saveResponse(responseDTO);

        if (HttpStatus.OK.value() != responseDTO.getStatus()) {
            return responseDTO;
        }
        responseDTO.setStatusDetails("Everything is fine");
        return responseDTO;
    }

    private void saveResponse(ResponseDTO responseDTO) {
        var userList = userClient.getUsers();
        int success = userServiceImpl.saveAll(userList, responseDTO);
        if (Objects.nonNull(userList) && success != userList.size()) {
            responseDTO.setStatusDetails(String.format("Can not save all records. Only %d success", success));
            responseDTO.setStatus(HttpStatus.EXPECTATION_FAILED.value());
        }
    }

    private void testReturningConsistency(ResponseDTO responseDTO) {
        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            int size = userClient.getUsers().size();
            if (responseDTO.getResultNumber() == 0) {
                responseDTO.setResultNumber(size);
                responseDTO.setSuccessCallCount(responseDTO.getSuccessCallCount() + 1);
            } else {
                size = userClient.getUsers().size();
                responseDTO.setSuccessCallCount(responseDTO.getSuccessCallCount() + 1);
                if (responseDTO.getResultNumber() != size) {
                    responseDTO.setStatus(HttpStatus.EXPECTATION_FAILED.value());
                    responseDTO.setStatusDetails("The returning count does not consequent.");
                }
            }
        }
        responseDTO.setStatus(HttpStatus.OK.value());
    }
}

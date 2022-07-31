package com.cubicfox.restApiTest.service;

import com.cubicfox.restApiTest.model.ResponseDTO;

public interface TestService {
    int NUMBER_OF_TRIES = 5;
    int getHttpStatus();

    ResponseDTO fullTest();

}

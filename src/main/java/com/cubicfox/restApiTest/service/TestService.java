package com.cubicfox.restApiTest.service;

import com.cubicfox.restApiTest.model.ResponseDTO;

public interface TestService {
    int getHttpStatus();

    ResponseDTO fullTest();

}

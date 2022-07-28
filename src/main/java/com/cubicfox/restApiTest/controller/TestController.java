package com.cubicfox.restApiTest.controller;

import com.cubicfox.restApiTest.model.ResponseDTO;
import com.cubicfox.restApiTest.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("test")
public class TestController {
    private final TestService testServiceImpl;

    public TestController(TestService testServiceImpl) {
        this.testServiceImpl = testServiceImpl;
    }

    @GetMapping("testAll")
    public ResponseEntity<ResponseDTO> test(){
        var result = testServiceImpl.fullTest();
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}

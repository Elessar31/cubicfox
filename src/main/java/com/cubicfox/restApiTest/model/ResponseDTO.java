package com.cubicfox.restApiTest.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder()
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO {
    private int status;
    private int successCallCount;
    private int successSaveUser;
    private int resultNumber;
    private String statusDetails;
    private List<String> errorList = new ArrayList<>();
}

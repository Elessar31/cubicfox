package com.cubicfox.restApiTest.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Geo{
    private BigDecimal lat;
    private BigDecimal lng;
}
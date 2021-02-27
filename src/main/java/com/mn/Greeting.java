package com.mn;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class Greeting {
    private String myText = "Hello everyone";
    private BigDecimal id = BigDecimal.valueOf(1242141);
    private Instant timeUTC = Instant.now();
}

package com.mn.broker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "Symbol",
        description = "Abbreviation to uniquely identify public trades shares of a stock"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Symbol {

    @Schema(name = "Stock value", minLength = 1, maxLength = 3)
    private String value;
}

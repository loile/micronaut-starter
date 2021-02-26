package com.mn;

import io.micronaut.context.annotation.ConfigurationInject;
import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(value = "hello.config.greeting")
public class GreetingConfiguration {

    @Getter
    private final String en;

    @Getter
    private final String de;

    @ConfigurationInject
    public GreetingConfiguration(@NotBlank String en, @NotBlank String de) {
        this.en = en;
        this.de = de;
    }
}

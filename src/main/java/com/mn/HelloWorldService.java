package com.mn;

import io.micronaut.context.annotation.Value;

import javax.inject.Singleton;

@Singleton
public class HelloWorldService {

    @Value("${hello.text:Hello from Service}")
    private String hello;

    public String sayHello() {
        return hello;
    }
}

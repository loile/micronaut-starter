package com.mn;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class HelloWorldControllerTest {

    @Inject
    @Client("/hello")
    RxHttpClient client;

    @Test
    void greet() {
        final ObjectNode result = this.client.toBlocking().retrieve(HttpRequest.GET("/greeting"), ObjectNode.class);
        assertEquals("someJSON", result.toString());
    }
}
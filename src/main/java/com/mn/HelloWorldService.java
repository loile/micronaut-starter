package com.mn;

import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class HelloWorldService {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldService.class);

    @Value("${hello.text:Hello from Service}")
    private String hello;

    public String sayHello() {
        return hello;
    }

    @EventListener
    public void startUp(StartupEvent startupEvent) {
        LOG.debug("Startup HelloWorldService ", startupEvent.toString());
    }
}

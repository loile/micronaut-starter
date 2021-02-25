package com.mn;


import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/hello")
public class HelloWorldController {

    private final HelloWorldService helloWorldService;
    private final GreetingConfiguration greetingConfiguration;

    public HelloWorldController(HelloWorldService helloWorldService, GreetingConfiguration greetingConfiguration) {
        this.helloWorldService = helloWorldService;
        this.greetingConfiguration = greetingConfiguration;
    }

    @Get
    public String index() {
        return helloWorldService.sayHello();
    }

    @Get("/de")
    public String greetingsInGerman() {
        return this.greetingConfiguration.getDe();
    }

    @Get("/en")
    public String greetingsInEn() {
        return this.greetingConfiguration.getEn();
    }

}

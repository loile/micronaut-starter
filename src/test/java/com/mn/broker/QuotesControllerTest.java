package com.mn.broker;

import com.mn.broker.model.Quote;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class QuotesControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void returnQuote() {
        final Quote quote1 = client.toBlocking().retrieve(HttpRequest.GET("/quotes/HSBC"), Quote.class);

        assertThat(quote1).isNotNull().extracting("symbol").extracting("value").isEqualTo("HSBC");

        final Quote quote2 = client.toBlocking().retrieve(HttpRequest.GET("/quotes/BNP"), Quote.class);

        assertThat(quote2).isNotNull().extracting("symbol").extracting("value").isEqualTo("BNP");
    }

}
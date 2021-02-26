package com.mn.broker;

import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class MarketsControllerTest {

    @Inject
    @Client("/") RxHttpClient client;

    @Test
    public void returnSymbolsList() {
        final List<LinkedHashMap<String, String>> result = this.client.toBlocking().retrieve("/markets", List.class);
        assertEquals(5, result.size());

        assertThat(result).extracting(entry -> entry.get("value")).
                containsExactlyInAnyOrder("AAPL", "ANZ", "VCB", "AGB", "HSBC");

    }

}
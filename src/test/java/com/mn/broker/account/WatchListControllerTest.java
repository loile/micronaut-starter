package com.mn.broker.account;

import com.mn.broker.model.Symbol;
import com.mn.broker.model.WatchList;
import com.mn.broker.store.InMemoryAccountStore;
import io.micronaut.http.*;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class WatchListControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    InMemoryAccountStore store;

    @Test
    public void getEmptyList() {

        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("my-user", "secret");

        var login = HttpRequest.POST("/login", credentials);
        final HttpResponse<BearerAccessRefreshToken> response = client.toBlocking().exchange(login, BearerAccessRefreshToken.class);

        assertEquals(HttpStatus.OK, response.getStatus());
        final BearerAccessRefreshToken token = response.body();
        assertNotNull(token);
        assertEquals("my-user", token.getUsername());

        LOG.debug("Login user token {} expires in {}", token.getAccessToken(), token.getExpiresIn());


        final MutableHttpRequest<Object> request = GET("/account/watchlist");
        request.accept(MediaType.APPLICATION_JSON).bearerAuth(token.getAccessToken());

        final WatchList watchList = this.client.toBlocking().retrieve(request, WatchList.class);

        assertThat(watchList).isNotNull();
        assertThat(watchList.getSymbols()).isEmpty();
    }

    @Test
    public void getNomEmptyWatchList() {
        final List<Symbol> symbols = Stream.of("BNP", "VCB", "ACB", "HSBC")
                .map(Symbol::new)
                .collect(Collectors.toList());

        final WatchList watchList = new WatchList(symbols);
        this.store.updateWatchList(WatchListController.ACCOUNT_ID, watchList);

        final WatchList result = this.client.toBlocking().retrieve(GET("/account/watchlist"), WatchList.class);

        assertThat(result).isNotNull();
        assertThat(result.getSymbols()).hasSize(4);
    }

    @Test
    public void updateWatchList() {
        final List<Symbol> symbols = Stream.of("BNP", "VCB", "ACB", "HSBC")
                .map(Symbol::new)
                .collect(Collectors.toList());

        final WatchList watchList = new WatchList(symbols);
        this.store.updateWatchList(WatchListController.ACCOUNT_ID, watchList);

        final WatchList result = this.client.toBlocking().retrieve(PUT("/account/watchlist", watchList), WatchList.class);

        assertThat(result).isNotNull();
        assertThat(result.getSymbols()).hasSize(4);
        assertThat(result).isEqualTo(watchList);
    }

    @Test
    public void deleteWatchList() {
        final List<Symbol> symbols = Stream.of("BNP", "VCB", "ACB", "HSBC")
                .map(Symbol::new)
                .collect(Collectors.toList());

        final WatchList watchList = new WatchList(symbols);
        final UUID accountId = UUID.randomUUID();
        this.store.updateWatchList(accountId, watchList);
        final HttpResponse<Object> response = this.client.toBlocking().exchange(DELETE("/account/watchlist/" + accountId));

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());
    }

}
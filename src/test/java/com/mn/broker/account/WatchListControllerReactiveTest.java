package com.mn.broker.account;

import com.mn.broker.model.Symbol;
import com.mn.broker.model.WatchList;
import com.mn.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class WatchListControllerReactiveTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactiveTest.class);

    @Inject
    @Client("/")
    JWTWatchListClient client;

    @Inject
    InMemoryAccountStore store;

    private WatchList watchList;

    @BeforeEach
    public void setUp() {
        final List<Symbol> symbols = Stream.of("BNP", "VCB", "ACB", "HSBC")
                .map(Symbol::new)
                .collect(Collectors.toList());

        this.watchList = new WatchList(symbols);
        this.store.updateWatchList(WatchListReactiveController.ACCOUNT_ID, watchList);
    }

    @Test
    public void getEmptyList() {
        this.store.updateWatchList(WatchListReactiveController.ACCOUNT_ID, new WatchList());

        final Single<WatchList> result = this.client.retrieveWatchList(getAccessToken()).singleOrError();

        assertThat(result.blockingGet().getSymbols()).isEmpty();
    }

    @Test
    public void getNonEmptyWatchList() {
        final Single<WatchList> result = this.client.retrieveWatchList(getAccessToken()).singleOrError();

        assertThat(result).isNotNull();
        assertThat(result.blockingGet().getSymbols()).hasSize(4);
    }

    @Test
    public void getAsSingleWatchList() {
        final Single<WatchList> result = this.client.retrieveWatchListAsASingle(getAccessToken());

        assertThat(result).isNotNull();
        assertThat(result.blockingGet().getSymbols()).hasSize(4);
    }

    @Test
    public void updateWatchList() {
        final HttpResponse<WatchList> result = this.client.updateWatchList(getAccessToken(), watchList);

        assertThat(result).isNotNull();
        assertThat(result.body().getSymbols()).hasSize(4);
    }

    @Test
    public void deleteWatchList() {
        final HttpResponse<WatchList> response = this.client.deleteWatchList(getAccessToken(), WatchListReactiveController.ACCOUNT_ID);

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());
    }

    private BearerAccessRefreshToken getBearerAccessRefreshToken() {
        return this.client.login(new UsernamePasswordCredentials("my-user", "secret"));
    }

    private String getAccessToken() {
        return "Bearer " + getBearerAccessRefreshToken().getAccessToken();
    }

}
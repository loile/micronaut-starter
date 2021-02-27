package com.mn.broker.account;

import com.mn.broker.model.Symbol;
import com.mn.broker.model.WatchList;
import com.mn.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class WatchListControllerReactiveTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactiveTest.class);

    @Inject
    @Client("/account/watchlist-reactive")
    RxHttpClient client;

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
        final Single<WatchList> watchList = this.client.retrieve(GET("/"), WatchList.class).singleOrError();

        assertThat(watchList).isNotNull();
        assertThat(watchList.blockingGet().getSymbols()).isEmpty();
    }

    @Test
    public void getNonEmptyWatchList() {
        final Single<WatchList> result = this.client.retrieve(GET("/"), WatchList.class).singleOrError();

        assertThat(result).isNotNull();
        assertThat(result.blockingGet().getSymbols()).hasSize(4);
    }

    @Test
    public void getAsSingleWatchList() {
        final WatchList result = this.client.toBlocking().retrieve(GET("/single"), WatchList.class);

        assertThat(result).isNotNull();
        assertThat(result.getSymbols()).hasSize(4);
    }

    @Test
    public void updateWatchList() {
        final WatchList result = this.client.toBlocking().retrieve(PUT("/", watchList), WatchList.class);

        assertThat(result).isNotNull();
        assertThat(result.getSymbols()).hasSize(4);
        assertThat(result).isEqualTo(watchList);
    }

    @Test
    public void deleteWatchList() {
        final HttpResponse<Object> response = this.client.toBlocking().exchange(DELETE("/" + WatchListReactiveController.ACCOUNT_ID));

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());
    }

}
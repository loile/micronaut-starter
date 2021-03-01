package com.mn.broker.account;

import com.mn.broker.model.WatchList;
import com.mn.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.UUID;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/account/watchlist")
public class WatchListController {

    private final InMemoryAccountStore store;
    static final UUID ACCOUNT_ID = UUID.randomUUID();

    public WatchListController(InMemoryAccountStore store) {
        this.store = store;
    }

    @Get(value = "/")
    public WatchList getWatchList() {
        return this.store.getWatchList(ACCOUNT_ID);
    }

    @Put(consumes = MediaType.APPLICATION_JSON,
         produces = MediaType.APPLICATION_JSON)
    public WatchList update(@Body WatchList watchList) {
        return this.store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete("/{accountId}")
    public HttpResponse delete(@PathVariable UUID accountId) {
        this.store.deleteWatchList(accountId);

        return HttpResponse.noContent();
    }
}

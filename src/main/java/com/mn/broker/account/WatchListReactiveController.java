package com.mn.broker.account;

import com.mn.broker.model.WatchList;
import com.mn.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/account/watchlist-reactive")
public class WatchListReactiveController {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListReactiveController.class);

    private final InMemoryAccountStore store;
    static final UUID ACCOUNT_ID = UUID.randomUUID();
    private final Scheduler scheduler;

    public WatchListReactiveController(InMemoryAccountStore store, @Named(TaskExecutors.IO) ExecutorService executorService) {
        this.store = store;
        this.scheduler = Schedulers.from(executorService);
    }

    @Get(value = "/")
    @ExecuteOn(TaskExecutors.IO)
    public WatchList getWatchList() {
        LOG.info("getWatchList - current thread {}", Thread.currentThread().getName());
        return this.store.getWatchList(ACCOUNT_ID);
    }

    @Get(
            value = "/single",
            produces = MediaType.APPLICATION_JSON
    )
    public Single<WatchList> getAsSingle() {
        return Single.fromCallable(() -> {
            LOG.info("getAsSingle - current thread {}", Thread.currentThread().getName());
            return this.store.getWatchList(ACCOUNT_ID);
        }).subscribeOn(scheduler);
    }

    @Put(consumes = MediaType.APPLICATION_JSON,
         produces = MediaType.APPLICATION_JSON)
    public WatchList updateWatchList(@Body WatchList watchList) {
        LOG.info("updateWatchList - current thread {}", Thread.currentThread().getName());
        return this.store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete("/{accountId}")
    public HttpResponse deleteWatchList(@PathVariable UUID accountId) {
        LOG.info("deleteWatchList - current thread {}", Thread.currentThread().getName());
        this.store.deleteWatchList(accountId);

        return HttpResponse.noContent();
    }
}

package com.mn.broker;

import com.mn.broker.model.Symbol;
import com.mn.broker.persistence.SymbolEntity;
import com.mn.broker.persistence.SymbolsRepository;
import com.mn.broker.store.InMemoryStore;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/markets")
public class MarketsController {

    private final InMemoryStore store;
    private final SymbolsRepository repository;

    public MarketsController(InMemoryStore store, SymbolsRepository repository) {
        this.store = store;
        this.repository = repository;
    }

    @Operation(summary = "return all available markets")
    @ApiResponse(
        content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Tag(name = "markets")
    @Get("/")
    public List<Symbol> getAllMarkets() {
        return this.store.getSymbols();
    }

    @Operation(summary = "return all available markets from database using JPA")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Tag(name = "markets")
    @Get("/jpa")
    public Single<List<SymbolEntity>> getAllMarketsViaJPA() {
        return Single.just(this.repository.findAll());
    }
}

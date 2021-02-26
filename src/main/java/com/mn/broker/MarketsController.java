package com.mn.broker;

import com.mn.broker.model.Symbol;
import com.mn.broker.store.InMemoryStore;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;

@Controller("/markets")
public class MarketsController {

    private final InMemoryStore store;

    public MarketsController(InMemoryStore store) {
        this.store = store;
    }

    @Get("/")
    public List<Symbol> getAllMarkets() {
        return this.store.getSymbols();
    }
}

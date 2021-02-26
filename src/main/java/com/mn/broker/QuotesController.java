package com.mn.broker;

import com.mn.broker.model.Quote;
import com.mn.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

@Controller("/quotes")
public class QuotesController {

    private final InMemoryStore store;

    public QuotesController(InMemoryStore store) {
        this.store = store;
    }

    @Get("/{symbol}")
    public HttpResponse getQuoteBySymbol(@PathVariable String symbol) {
        Quote quote = store.getQuoteBySymbol(symbol);

        return HttpResponse.ok(quote);
    }

}

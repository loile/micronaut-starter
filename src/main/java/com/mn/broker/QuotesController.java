package com.mn.broker;

import com.mn.broker.model.Quote;
import com.mn.broker.persistence.QuoteEntity;
import com.mn.broker.persistence.QuotesRepository;
import com.mn.broker.store.InMemoryStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Secured({SecurityRule.IS_ANONYMOUS})
@Controller("/quotes")
public class QuotesController {

    private final InMemoryStore store;
    private final QuotesRepository quotesRepository;


    public QuotesController(InMemoryStore store, QuotesRepository quotesRepository) {
        this.store = store;
        this.quotesRepository = quotesRepository;
    }

    @Operation(summary = "Return a quote for a given symbol")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
    @Tag(name = "quotes")
    @Get("/{symbol}")
    public HttpResponse getQuoteBySymbol(@PathVariable String symbol) {
        Quote quote = store.getQuoteBySymbol(symbol);

        return HttpResponse.ok(quote);
    }

    @Get("/jpa")
    public List<QuoteEntity> getAllQuotesViaJPA() {
        return quotesRepository.findAll();
    }

    @Get("/ordered/desc")
    public List<QuoteEntity> orderedDesc() {
        return this.quotesRepository.listOrderByVolumeDesc();
    }

    @Get("/ordered/asc")
    public List<QuoteEntity> orderedAsc() {
        return this.quotesRepository.listOrderByVolumeAsc();
    }
}

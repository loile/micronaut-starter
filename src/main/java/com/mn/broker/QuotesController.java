package com.mn.broker;

import com.mn.broker.model.Quote;
import com.mn.broker.persistence.QuoteDTO;
import com.mn.broker.persistence.QuoteEntity;
import com.mn.broker.persistence.QuotesRepository;
import com.mn.broker.store.InMemoryStore;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    public List<QuoteDTO> orderedDesc() {
        return this.quotesRepository.listOrderByVolumeDesc();
    }

    @Get("/ordered/asc")
    public List<QuoteDTO> orderedAsc() {
        return this.quotesRepository.listOrderByVolumeAsc();
    }

    @Get("/volume/{volume}")
    public List<QuoteDTO> volumeFilter(@PathVariable BigDecimal volume) {
        return this.quotesRepository.findByVolumeGreaterThanOrderByVolumeAsc(volume);
    }

    @Get("/jpa/pagination{?page,volume}")
    public List<QuoteDTO> volumeFilterPagination(@QueryValue Optional<Integer> page, @QueryValue Optional<BigDecimal> volume) {
        var myVolume = volume.isPresent() ? volume.get() : BigDecimal.ZERO;
        var myPage = page.isPresent() ? page.get() : 0;
        return this.quotesRepository.findByVolumeGreaterThan(myVolume, Pageable.from(myPage, 5));
    }

    @Get("/jpa/pagination/{page}")
    public List<QuoteDTO> slicePagination(@PathVariable Integer page) {
        return this.quotesRepository.list(Pageable.from(page, 5)).getContent();
    }
}

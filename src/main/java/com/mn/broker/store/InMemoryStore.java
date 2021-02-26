package com.mn.broker.store;

import com.mn.broker.model.Quote;
import com.mn.broker.model.Symbol;
import lombok.Getter;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {

    @Getter
    private List<Symbol> symbols;

    @Getter
    private Map<String, Quote> quotes;

    public InMemoryStore() {
        symbols = Stream.of("AAPL", "ANZ", "VCB", "AGB", "HSBC")
                .map(Symbol::new)
                .collect(Collectors.toList());

        quotes = symbols.stream()
                    .collect(Collectors.toMap(Symbol::getValue, symbol -> Quote.builder().symbol(symbol).build()));

    }

    public Quote getQuoteBySymbol(String symbol) {
        final Quote quote = Quote.builder()
                                .symbol(new Symbol(symbol))
                                .build();
        quotes.putIfAbsent(symbol, quote);

        return quotes.get(symbol);
    }
}

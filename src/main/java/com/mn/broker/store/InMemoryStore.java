package com.mn.broker.store;

import com.mn.broker.model.Symbol;
import lombok.Getter;

import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {

    @Getter
    private List<Symbol> symbols;

    public InMemoryStore() {
        symbols = Stream.of("AAPL", "ANZ", "VCB", "AGB", "HSBC")
                .map(Symbol::new)
                .collect(Collectors.toList());
    }

}

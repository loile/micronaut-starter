package com.mn.broker.persistence;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Singleton
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private final SymbolsRepository symbolsRepository;
    private final QuotesRepository quotesRepository;

    public TestDataProvider(SymbolsRepository repository, QuotesRepository quotesRepository) {
        this.symbolsRepository = repository;
        this.quotesRepository = quotesRepository;
    }

    @EventListener
    public void init(StartupEvent startupEvent) {

        if (symbolsRepository.findAll().isEmpty()) {
            LOG.debug("Add symbols testing data");
            Stream.of("HSBC", "ANZ", "NAB", "VCB", "ACB")
                    .map(SymbolEntity::new)
                    .forEach(symbolsRepository::save);
        }

        if (quotesRepository.findAll().isEmpty()) {
            LOG.debug("Adding quote data testing");
            this.symbolsRepository.findAll().forEach(symbolEntity -> {
                final QuoteEntity quoteEntity = QuoteEntity.builder()
                        .symbol(symbolEntity)
                        .ask(randomValue())
                        .bid(randomValue())
                        .lastPrice(randomValue())
                        .build();

                quotesRepository.save(quoteEntity);
            });
        }
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(RANDOM.nextDouble(0, 100));
    }
}

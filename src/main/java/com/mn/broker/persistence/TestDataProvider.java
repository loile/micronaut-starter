package com.mn.broker.persistence;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.stream.Stream;

@Singleton
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
    private final SymbolsRepository repository;

    public TestDataProvider(SymbolsRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void init(StartupEvent startupEvent) {

        if (repository.findAll().isEmpty()) {
            LOG.debug("Add symbols testing data");
            Stream.of("HSBC", "ANZ", "NAB", "VCB", "ACB")
                    .map(SymbolEntity::new)
                    .forEach(repository::save);
        }
    }
}

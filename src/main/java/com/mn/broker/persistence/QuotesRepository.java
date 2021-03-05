package com.mn.broker.persistence;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Integer> {

    @Override
    List<QuoteEntity> findAll();
}

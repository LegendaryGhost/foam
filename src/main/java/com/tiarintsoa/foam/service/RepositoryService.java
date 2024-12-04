package com.tiarintsoa.foam.service;

import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class RepositoryService {

    public Long getMaxIdOrDefault(Supplier<Long> repositoryMethod, Long defaultValue) {
        Long maxId = repositoryMethod.get();
        return maxId == null ? defaultValue : maxId;
    }

}

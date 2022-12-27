package com.calvin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionCategoryMapperTest {

    private CollectionCategoryMapper collectionCategoryMapper;

    @BeforeEach
    public void setUp() {
        collectionCategoryMapper = new CollectionCategoryMapperImpl();
    }
}

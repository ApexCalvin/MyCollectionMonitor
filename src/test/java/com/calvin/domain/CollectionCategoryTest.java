package com.calvin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.calvin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollectionCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionCategory.class);
        CollectionCategory collectionCategory1 = new CollectionCategory();
        collectionCategory1.setId(1L);
        CollectionCategory collectionCategory2 = new CollectionCategory();
        collectionCategory2.setId(collectionCategory1.getId());
        assertThat(collectionCategory1).isEqualTo(collectionCategory2);
        collectionCategory2.setId(2L);
        assertThat(collectionCategory1).isNotEqualTo(collectionCategory2);
        collectionCategory1.setId(null);
        assertThat(collectionCategory1).isNotEqualTo(collectionCategory2);
    }
}

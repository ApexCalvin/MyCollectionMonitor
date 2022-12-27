package com.calvin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.calvin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollectionCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionCategoryDTO.class);
        CollectionCategoryDTO collectionCategoryDTO1 = new CollectionCategoryDTO();
        collectionCategoryDTO1.setId(1L);
        CollectionCategoryDTO collectionCategoryDTO2 = new CollectionCategoryDTO();
        assertThat(collectionCategoryDTO1).isNotEqualTo(collectionCategoryDTO2);
        collectionCategoryDTO2.setId(collectionCategoryDTO1.getId());
        assertThat(collectionCategoryDTO1).isEqualTo(collectionCategoryDTO2);
        collectionCategoryDTO2.setId(2L);
        assertThat(collectionCategoryDTO1).isNotEqualTo(collectionCategoryDTO2);
        collectionCategoryDTO1.setId(null);
        assertThat(collectionCategoryDTO1).isNotEqualTo(collectionCategoryDTO2);
    }
}

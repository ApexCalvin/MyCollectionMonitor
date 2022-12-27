package com.calvin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.calvin.IntegrationTest;
import com.calvin.domain.CollectionCategory;
import com.calvin.repository.CollectionCategoryRepository;
import com.calvin.service.CollectionCategoryService;
import com.calvin.service.dto.CollectionCategoryDTO;
import com.calvin.service.mapper.CollectionCategoryMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CollectionCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CollectionCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/collection-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CollectionCategoryRepository collectionCategoryRepository;

    @Mock
    private CollectionCategoryRepository collectionCategoryRepositoryMock;

    @Autowired
    private CollectionCategoryMapper collectionCategoryMapper;

    @Mock
    private CollectionCategoryService collectionCategoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollectionCategoryMockMvc;

    private CollectionCategory collectionCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionCategory createEntity(EntityManager em) {
        CollectionCategory collectionCategory = new CollectionCategory().categoryName(DEFAULT_CATEGORY_NAME);
        return collectionCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionCategory createUpdatedEntity(EntityManager em) {
        CollectionCategory collectionCategory = new CollectionCategory().categoryName(UPDATED_CATEGORY_NAME);
        return collectionCategory;
    }

    @BeforeEach
    public void initTest() {
        collectionCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createCollectionCategory() throws Exception {
        int databaseSizeBeforeCreate = collectionCategoryRepository.findAll().size();
        // Create the CollectionCategory
        CollectionCategoryDTO collectionCategoryDTO = collectionCategoryMapper.toDto(collectionCategory);
        restCollectionCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        CollectionCategory testCollectionCategory = collectionCategoryList.get(collectionCategoryList.size() - 1);
        assertThat(testCollectionCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void createCollectionCategoryWithExistingId() throws Exception {
        // Create the CollectionCategory with an existing ID
        collectionCategory.setId(1L);
        CollectionCategoryDTO collectionCategoryDTO = collectionCategoryMapper.toDto(collectionCategory);

        int databaseSizeBeforeCreate = collectionCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectionCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCollectionCategories() throws Exception {
        // Initialize the database
        collectionCategoryRepository.saveAndFlush(collectionCategory);

        // Get all the collectionCategoryList
        restCollectionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectionCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCollectionCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(collectionCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCollectionCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(collectionCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCollectionCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(collectionCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCollectionCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(collectionCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCollectionCategory() throws Exception {
        // Initialize the database
        collectionCategoryRepository.saveAndFlush(collectionCategory);

        // Get the collectionCategory
        restCollectionCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, collectionCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collectionCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCollectionCategory() throws Exception {
        // Get the collectionCategory
        restCollectionCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCollectionCategory() throws Exception {
        // Initialize the database
        collectionCategoryRepository.saveAndFlush(collectionCategory);

        int databaseSizeBeforeUpdate = collectionCategoryRepository.findAll().size();

        // Update the collectionCategory
        CollectionCategory updatedCollectionCategory = collectionCategoryRepository.findById(collectionCategory.getId()).get();
        // Disconnect from session so that the updates on updatedCollectionCategory are not directly saved in db
        em.detach(updatedCollectionCategory);
        updatedCollectionCategory.categoryName(UPDATED_CATEGORY_NAME);
        CollectionCategoryDTO collectionCategoryDTO = collectionCategoryMapper.toDto(updatedCollectionCategory);

        restCollectionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeUpdate);
        CollectionCategory testCollectionCategory = collectionCategoryList.get(collectionCategoryList.size() - 1);
        assertThat(testCollectionCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCollectionCategory() throws Exception {
        int databaseSizeBeforeUpdate = collectionCategoryRepository.findAll().size();
        collectionCategory.setId(count.incrementAndGet());

        // Create the CollectionCategory
        CollectionCategoryDTO collectionCategoryDTO = collectionCategoryMapper.toDto(collectionCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollectionCategory() throws Exception {
        int databaseSizeBeforeUpdate = collectionCategoryRepository.findAll().size();
        collectionCategory.setId(count.incrementAndGet());

        // Create the CollectionCategory
        CollectionCategoryDTO collectionCategoryDTO = collectionCategoryMapper.toDto(collectionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollectionCategory() throws Exception {
        int databaseSizeBeforeUpdate = collectionCategoryRepository.findAll().size();
        collectionCategory.setId(count.incrementAndGet());

        // Create the CollectionCategory
        CollectionCategoryDTO collectionCategoryDTO = collectionCategoryMapper.toDto(collectionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCollectionCategoryWithPatch() throws Exception {
        // Initialize the database
        collectionCategoryRepository.saveAndFlush(collectionCategory);

        int databaseSizeBeforeUpdate = collectionCategoryRepository.findAll().size();

        // Update the collectionCategory using partial update
        CollectionCategory partialUpdatedCollectionCategory = new CollectionCategory();
        partialUpdatedCollectionCategory.setId(collectionCategory.getId());

        restCollectionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollectionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollectionCategory))
            )
            .andExpect(status().isOk());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeUpdate);
        CollectionCategory testCollectionCategory = collectionCategoryList.get(collectionCategoryList.size() - 1);
        assertThat(testCollectionCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCollectionCategoryWithPatch() throws Exception {
        // Initialize the database
        collectionCategoryRepository.saveAndFlush(collectionCategory);

        int databaseSizeBeforeUpdate = collectionCategoryRepository.findAll().size();

        // Update the collectionCategory using partial update
        CollectionCategory partialUpdatedCollectionCategory = new CollectionCategory();
        partialUpdatedCollectionCategory.setId(collectionCategory.getId());

        partialUpdatedCollectionCategory.categoryName(UPDATED_CATEGORY_NAME);

        restCollectionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollectionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollectionCategory))
            )
            .andExpect(status().isOk());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeUpdate);
        CollectionCategory testCollectionCategory = collectionCategoryList.get(collectionCategoryList.size() - 1);
        assertThat(testCollectionCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCollectionCategory() throws Exception {
        int databaseSizeBeforeUpdate = collectionCategoryRepository.findAll().size();
        collectionCategory.setId(count.incrementAndGet());

        // Create the CollectionCategory
        CollectionCategoryDTO collectionCategoryDTO = collectionCategoryMapper.toDto(collectionCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collectionCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollectionCategory() throws Exception {
        int databaseSizeBeforeUpdate = collectionCategoryRepository.findAll().size();
        collectionCategory.setId(count.incrementAndGet());

        // Create the CollectionCategory
        CollectionCategoryDTO collectionCategoryDTO = collectionCategoryMapper.toDto(collectionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollectionCategory() throws Exception {
        int databaseSizeBeforeUpdate = collectionCategoryRepository.findAll().size();
        collectionCategory.setId(count.incrementAndGet());

        // Create the CollectionCategory
        CollectionCategoryDTO collectionCategoryDTO = collectionCategoryMapper.toDto(collectionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollectionCategory in the database
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCollectionCategory() throws Exception {
        // Initialize the database
        collectionCategoryRepository.saveAndFlush(collectionCategory);

        int databaseSizeBeforeDelete = collectionCategoryRepository.findAll().size();

        // Delete the collectionCategory
        restCollectionCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, collectionCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CollectionCategory> collectionCategoryList = collectionCategoryRepository.findAll();
        assertThat(collectionCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

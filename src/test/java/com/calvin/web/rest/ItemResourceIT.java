package com.calvin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.calvin.IntegrationTest;
import com.calvin.domain.Item;
import com.calvin.repository.ItemRepository;
import com.calvin.service.dto.ItemDTO;
import com.calvin.service.mapper.ItemMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELEASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_RELEASE_PRICE = 1D;
    private static final Double UPDATED_RELEASE_PRICE = 2D;

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_PURCHASE_PRICE = 1D;
    private static final Double UPDATED_PURCHASE_PRICE = 2D;

    private static final Double DEFAULT_MARKET_PRICE = 1D;
    private static final Double UPDATED_MARKET_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .name(DEFAULT_NAME)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .releasePrice(DEFAULT_RELEASE_PRICE)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .purchasePrice(DEFAULT_PURCHASE_PRICE)
            .marketPrice(DEFAULT_MARKET_PRICE);
        return item;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity(EntityManager em) {
        Item item = new Item()
            .name(UPDATED_NAME)
            .releaseDate(UPDATED_RELEASE_DATE)
            .releasePrice(UPDATED_RELEASE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .marketPrice(UPDATED_MARKET_PRICE);
        return item;
    }

    @BeforeEach
    public void initTest() {
        item = createEntity(em);
    }

    @Test
    @Transactional
    void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();
        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItem.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testItem.getReleasePrice()).isEqualTo(DEFAULT_RELEASE_PRICE);
        assertThat(testItem.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testItem.getPurchasePrice()).isEqualTo(DEFAULT_PURCHASE_PRICE);
        assertThat(testItem.getMarketPrice()).isEqualTo(DEFAULT_MARKET_PRICE);
    }

    @Test
    @Transactional
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId(1L);
        ItemDTO itemDTO = itemMapper.toDto(item);

        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].releasePrice").value(hasItem(DEFAULT_RELEASE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(DEFAULT_PURCHASE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].marketPrice").value(hasItem(DEFAULT_MARKET_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc
            .perform(get(ENTITY_API_URL_ID, item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.releasePrice").value(DEFAULT_RELEASE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.purchasePrice").value(DEFAULT_PURCHASE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.marketPrice").value(DEFAULT_MARKET_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).get();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .name(UPDATED_NAME)
            .releaseDate(UPDATED_RELEASE_DATE)
            .releasePrice(UPDATED_RELEASE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .marketPrice(UPDATED_MARKET_PRICE);
        ItemDTO itemDTO = itemMapper.toDto(updatedItem);

        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItem.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testItem.getReleasePrice()).isEqualTo(UPDATED_RELEASE_PRICE);
        assertThat(testItem.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testItem.getPurchasePrice()).isEqualTo(UPDATED_PURCHASE_PRICE);
        assertThat(testItem.getMarketPrice()).isEqualTo(UPDATED_MARKET_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem.purchaseDate(UPDATED_PURCHASE_DATE);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItem.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testItem.getReleasePrice()).isEqualTo(DEFAULT_RELEASE_PRICE);
        assertThat(testItem.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testItem.getPurchasePrice()).isEqualTo(DEFAULT_PURCHASE_PRICE);
        assertThat(testItem.getMarketPrice()).isEqualTo(DEFAULT_MARKET_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .name(UPDATED_NAME)
            .releaseDate(UPDATED_RELEASE_DATE)
            .releasePrice(UPDATED_RELEASE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .marketPrice(UPDATED_MARKET_PRICE);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItem.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testItem.getReleasePrice()).isEqualTo(UPDATED_RELEASE_PRICE);
        assertThat(testItem.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testItem.getPurchasePrice()).isEqualTo(UPDATED_PURCHASE_PRICE);
        assertThat(testItem.getMarketPrice()).isEqualTo(UPDATED_MARKET_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Delete the item
        restItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, item.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

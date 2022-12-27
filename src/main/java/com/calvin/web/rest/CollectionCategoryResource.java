package com.calvin.web.rest;

import com.calvin.repository.CollectionCategoryRepository;
import com.calvin.service.CollectionCategoryService;
import com.calvin.service.dto.CollectionCategoryDTO;
import com.calvin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.calvin.domain.CollectionCategory}.
 */
@RestController
@RequestMapping("/api")
public class CollectionCategoryResource {

    private final Logger log = LoggerFactory.getLogger(CollectionCategoryResource.class);

    private static final String ENTITY_NAME = "collectionCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollectionCategoryService collectionCategoryService;

    private final CollectionCategoryRepository collectionCategoryRepository;

    public CollectionCategoryResource(
        CollectionCategoryService collectionCategoryService,
        CollectionCategoryRepository collectionCategoryRepository
    ) {
        this.collectionCategoryService = collectionCategoryService;
        this.collectionCategoryRepository = collectionCategoryRepository;
    }

    /**
     * {@code POST  /collection-categories} : Create a new collectionCategory.
     *
     * @param collectionCategoryDTO the collectionCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collectionCategoryDTO, or with status {@code 400 (Bad Request)} if the collectionCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collection-categories")
    public ResponseEntity<CollectionCategoryDTO> createCollectionCategory(@RequestBody CollectionCategoryDTO collectionCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save CollectionCategory : {}", collectionCategoryDTO);
        if (collectionCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new collectionCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollectionCategoryDTO result = collectionCategoryService.save(collectionCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/collection-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collection-categories/:id} : Updates an existing collectionCategory.
     *
     * @param id the id of the collectionCategoryDTO to save.
     * @param collectionCategoryDTO the collectionCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the collectionCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collectionCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collection-categories/{id}")
    public ResponseEntity<CollectionCategoryDTO> updateCollectionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CollectionCategoryDTO collectionCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CollectionCategory : {}, {}", id, collectionCategoryDTO);
        if (collectionCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CollectionCategoryDTO result = collectionCategoryService.update(collectionCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collectionCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /collection-categories/:id} : Partial updates given fields of an existing collectionCategory, field will ignore if it is null
     *
     * @param id the id of the collectionCategoryDTO to save.
     * @param collectionCategoryDTO the collectionCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the collectionCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the collectionCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the collectionCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/collection-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CollectionCategoryDTO> partialUpdateCollectionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CollectionCategoryDTO collectionCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CollectionCategory partially : {}, {}", id, collectionCategoryDTO);
        if (collectionCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CollectionCategoryDTO> result = collectionCategoryService.partialUpdate(collectionCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collectionCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /collection-categories} : get all the collectionCategories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectionCategories in body.
     */
    @GetMapping("/collection-categories")
    public List<CollectionCategoryDTO> getAllCollectionCategories(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all CollectionCategories");
        return collectionCategoryService.findAll();
    }

    /**
     * {@code GET  /collection-categories/:id} : get the "id" collectionCategory.
     *
     * @param id the id of the collectionCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collectionCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collection-categories/{id}")
    public ResponseEntity<CollectionCategoryDTO> getCollectionCategory(@PathVariable Long id) {
        log.debug("REST request to get CollectionCategory : {}", id);
        Optional<CollectionCategoryDTO> collectionCategoryDTO = collectionCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collectionCategoryDTO);
    }

    /**
     * {@code DELETE  /collection-categories/:id} : delete the "id" collectionCategory.
     *
     * @param id the id of the collectionCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collection-categories/{id}")
    public ResponseEntity<Void> deleteCollectionCategory(@PathVariable Long id) {
        log.debug("REST request to delete CollectionCategory : {}", id);
        collectionCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.calvin.service;

import com.calvin.domain.CollectionCategory;
import com.calvin.repository.CollectionCategoryRepository;
import com.calvin.service.dto.CollectionCategoryDTO;
import com.calvin.service.mapper.CollectionCategoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CollectionCategory}.
 */
@Service
@Transactional
public class CollectionCategoryService {

    private final Logger log = LoggerFactory.getLogger(CollectionCategoryService.class);

    private final CollectionCategoryRepository collectionCategoryRepository;

    private final CollectionCategoryMapper collectionCategoryMapper;

    public CollectionCategoryService(
        CollectionCategoryRepository collectionCategoryRepository,
        CollectionCategoryMapper collectionCategoryMapper
    ) {
        this.collectionCategoryRepository = collectionCategoryRepository;
        this.collectionCategoryMapper = collectionCategoryMapper;
    }

    /**
     * Save a collectionCategory.
     *
     * @param collectionCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public CollectionCategoryDTO save(CollectionCategoryDTO collectionCategoryDTO) {
        log.debug("Request to save CollectionCategory : {}", collectionCategoryDTO);
        CollectionCategory collectionCategory = collectionCategoryMapper.toEntity(collectionCategoryDTO);
        collectionCategory = collectionCategoryRepository.save(collectionCategory);
        return collectionCategoryMapper.toDto(collectionCategory);
    }

    /**
     * Update a collectionCategory.
     *
     * @param collectionCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public CollectionCategoryDTO update(CollectionCategoryDTO collectionCategoryDTO) {
        log.debug("Request to update CollectionCategory : {}", collectionCategoryDTO);
        CollectionCategory collectionCategory = collectionCategoryMapper.toEntity(collectionCategoryDTO);
        collectionCategory = collectionCategoryRepository.save(collectionCategory);
        return collectionCategoryMapper.toDto(collectionCategory);
    }

    /**
     * Partially update a collectionCategory.
     *
     * @param collectionCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CollectionCategoryDTO> partialUpdate(CollectionCategoryDTO collectionCategoryDTO) {
        log.debug("Request to partially update CollectionCategory : {}", collectionCategoryDTO);

        return collectionCategoryRepository
            .findById(collectionCategoryDTO.getId())
            .map(existingCollectionCategory -> {
                collectionCategoryMapper.partialUpdate(existingCollectionCategory, collectionCategoryDTO);

                return existingCollectionCategory;
            })
            .map(collectionCategoryRepository::save)
            .map(collectionCategoryMapper::toDto);
    }

    /**
     * Get all the collectionCategories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CollectionCategoryDTO> findAll() {
        log.debug("Request to get all CollectionCategories");
        return collectionCategoryRepository
            .findAll()
            .stream()
            .map(collectionCategoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the collectionCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CollectionCategoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return collectionCategoryRepository.findAllWithEagerRelationships(pageable).map(collectionCategoryMapper::toDto);
    }

    /**
     * Get one collectionCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CollectionCategoryDTO> findOne(Long id) {
        log.debug("Request to get CollectionCategory : {}", id);
        return collectionCategoryRepository.findOneWithEagerRelationships(id).map(collectionCategoryMapper::toDto);
    }

    /**
     * Delete the collectionCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CollectionCategory : {}", id);
        collectionCategoryRepository.deleteById(id);
    }
}

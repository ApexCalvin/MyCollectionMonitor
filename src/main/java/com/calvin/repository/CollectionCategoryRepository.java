package com.calvin.repository;

import com.calvin.domain.CollectionCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CollectionCategory entity.
 */
@Repository
public interface CollectionCategoryRepository extends JpaRepository<CollectionCategory, Long> {
    @Query(
        "select collectionCategory from CollectionCategory collectionCategory where collectionCategory.user.login = ?#{principal.username}"
    )
    List<CollectionCategory> findByUserIsCurrentUser();

    default Optional<CollectionCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CollectionCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CollectionCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct collectionCategory from CollectionCategory collectionCategory left join fetch collectionCategory.user",
        countQuery = "select count(distinct collectionCategory) from CollectionCategory collectionCategory"
    )
    Page<CollectionCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct collectionCategory from CollectionCategory collectionCategory left join fetch collectionCategory.user")
    List<CollectionCategory> findAllWithToOneRelationships();

    @Query(
        "select collectionCategory from CollectionCategory collectionCategory left join fetch collectionCategory.user where collectionCategory.id =:id"
    )
    Optional<CollectionCategory> findOneWithToOneRelationships(@Param("id") Long id);
}

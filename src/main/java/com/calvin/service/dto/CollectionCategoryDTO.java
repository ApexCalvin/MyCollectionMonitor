package com.calvin.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.calvin.domain.CollectionCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CollectionCategoryDTO implements Serializable {

    private Long id;

    private String categoryName;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectionCategoryDTO)) {
            return false;
        }

        CollectionCategoryDTO collectionCategoryDTO = (CollectionCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, collectionCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollectionCategoryDTO{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", user=" + getUser() +
            "}";
    }
}

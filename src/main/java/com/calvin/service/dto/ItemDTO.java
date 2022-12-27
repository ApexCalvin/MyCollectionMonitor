package com.calvin.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.calvin.domain.Item} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate releaseDate;

    private Double releasePrice;

    private LocalDate purchaseDate;

    private Double purchasePrice;

    private Double marketPrice;

    private CollectionCategoryDTO collectionCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getReleasePrice() {
        return releasePrice;
    }

    public void setReleasePrice(Double releasePrice) {
        this.releasePrice = releasePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public CollectionCategoryDTO getCollectionCategory() {
        return collectionCategory;
    }

    public void setCollectionCategory(CollectionCategoryDTO collectionCategory) {
        this.collectionCategory = collectionCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        ItemDTO itemDTO = (ItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", releasePrice=" + getReleasePrice() +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", purchasePrice=" + getPurchasePrice() +
            ", marketPrice=" + getMarketPrice() +
            ", collectionCategory=" + getCollectionCategory() +
            "}";
    }
}

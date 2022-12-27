package com.calvin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "release_price")
    private Double releasePrice;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_price")
    private Double purchasePrice;

    @Column(name = "market_price")
    private Double marketPrice;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "items" }, allowSetters = true)
    private CollectionCategory collectionCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Item name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public Item releaseDate(LocalDate releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getReleasePrice() {
        return this.releasePrice;
    }

    public Item releasePrice(Double releasePrice) {
        this.setReleasePrice(releasePrice);
        return this;
    }

    public void setReleasePrice(Double releasePrice) {
        this.releasePrice = releasePrice;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public Item purchaseDate(LocalDate purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getPurchasePrice() {
        return this.purchasePrice;
    }

    public Item purchasePrice(Double purchasePrice) {
        this.setPurchasePrice(purchasePrice);
        return this;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getMarketPrice() {
        return this.marketPrice;
    }

    public Item marketPrice(Double marketPrice) {
        this.setMarketPrice(marketPrice);
        return this;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public CollectionCategory getCollectionCategory() {
        return this.collectionCategory;
    }

    public void setCollectionCategory(CollectionCategory collectionCategory) {
        this.collectionCategory = collectionCategory;
    }

    public Item collectionCategory(CollectionCategory collectionCategory) {
        this.setCollectionCategory(collectionCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", releasePrice=" + getReleasePrice() +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", purchasePrice=" + getPurchasePrice() +
            ", marketPrice=" + getMarketPrice() +
            "}";
    }
}

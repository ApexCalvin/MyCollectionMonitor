package com.calvin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CollectionCategory.
 */
@Entity
@Table(name = "collection_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CollectionCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "collectionCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "collectionCategory" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CollectionCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public CollectionCategory categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CollectionCategory user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setCollectionCategory(null));
        }
        if (items != null) {
            items.forEach(i -> i.setCollectionCategory(this));
        }
        this.items = items;
    }

    public CollectionCategory items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public CollectionCategory addItem(Item item) {
        this.items.add(item);
        item.setCollectionCategory(this);
        return this;
    }

    public CollectionCategory removeItem(Item item) {
        this.items.remove(item);
        item.setCollectionCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectionCategory)) {
            return false;
        }
        return id != null && id.equals(((CollectionCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollectionCategory{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            "}";
    }
}

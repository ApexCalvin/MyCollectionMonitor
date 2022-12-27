package com.calvin.service.mapper;

import com.calvin.domain.CollectionCategory;
import com.calvin.domain.Item;
import com.calvin.service.dto.CollectionCategoryDTO;
import com.calvin.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "collectionCategory", source = "collectionCategory", qualifiedByName = "collectionCategoryId")
    ItemDTO toDto(Item s);

    @Named("collectionCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CollectionCategoryDTO toDtoCollectionCategoryId(CollectionCategory collectionCategory);
}

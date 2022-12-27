package com.calvin.service.mapper;

import com.calvin.domain.CollectionCategory;
import com.calvin.domain.User;
import com.calvin.service.dto.CollectionCategoryDTO;
import com.calvin.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CollectionCategory} and its DTO {@link CollectionCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CollectionCategoryMapper extends EntityMapper<CollectionCategoryDTO, CollectionCategory> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    CollectionCategoryDTO toDto(CollectionCategory s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}

package com.pap.tech.mapper;

import com.pap.tech.dto.response.CategoryResponse;
import com.pap.tech.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "children", ignore = true)
    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toCategoryResponseList(List<Category> categories);
}

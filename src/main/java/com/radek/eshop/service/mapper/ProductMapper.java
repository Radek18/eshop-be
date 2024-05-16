package com.radek.eshop.service.mapper;

import com.radek.eshop.domain.Product;
import com.radek.eshop.service.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ProductMapper extends EntityMapper<ProductDto, Product> {
}

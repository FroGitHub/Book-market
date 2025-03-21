package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cart.CartDto;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class,
        uses = {BookMapper.class, CartItemsMapper.class})
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItemDtos",
            source = "cart.cartItems",
            qualifiedByName = "cartItemsToDto")
    CartDto toDto(Cart cart);

    @Mapping(target = "user", ignore = true)
    Cart toModel(CartDto cartDto);

    @AfterMapping
    default void implUser(@MappingTarget Cart cart, CartDto cartDto, @Context User user) {
        cart.setUser(user);
    }
}

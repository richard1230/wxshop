package com.github.wxshop.dao;
import com.github.wxshop.entity.ShoppingCartData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShoppingCartQueryMapper {
    int countHowManyShopsInUserShoppingCart(long userId);

    List<ShoppingCartData> selectShoppingCartDataByUserId(
            @Param("userId") long userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}


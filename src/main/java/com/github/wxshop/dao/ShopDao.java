package com.github.wxshop.dao;

import com.github.wxshop.generate.Shop;
import com.github.wxshop.generate.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopDao {

    //注入mapper的方法
    private final ShopMapper shopMapper;

    @Autowired
    public ShopDao(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    public Shop findShopById(Long shopId) {
        return shopMapper.selectByPrimaryKey(shopId);
    }
}

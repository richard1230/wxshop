package com.github.wxshop.service;

import com.github.api.DataStatus;
import com.github.api.data.PageResponse;
import com.github.api.exceptions.HttpException;
import com.github.wxshop.generate.Shop;
import com.github.wxshop.generate.ShopExample;
import com.github.wxshop.generate.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ShopService {
    private ShopMapper shopMapper;

    @Autowired
    public ShopService(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }


    public PageResponse<Shop> getShopByUserId(Long userId, Integer pageNum, Integer pageSize) {
        ShopExample countByStatus = new ShopExample();
        countByStatus.createCriteria()
                .andStatusEqualTo(DataStatus.OK.getName())
                .andOwnerUserIdEqualTo(userId);
        int totalNumber = (int) shopMapper.countByExample(countByStatus);
        int totalPage = totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1;

        ShopExample pageCondition = new ShopExample();
        pageCondition.setOrderByClause("updated_at desc");
        pageCondition.createCriteria()
                .andStatusEqualTo(DataStatus.OK.getName())
                .andOwnerUserIdEqualTo(userId);
        pageCondition.setLimit(pageSize);
        pageCondition.setOffset((pageNum - 1) * pageSize);

        List<Shop> pagedShops = shopMapper.selectByExample(pageCondition);

        return PageResponse.pagedData(pageNum, pageSize, totalPage, pagedShops);
    }

    public Shop createShop(Shop shop, Long creatorId) {
        shop.setOwnerUserId(creatorId);

        shop.setCreatedAt(new Date());
        shop.setUpdatedAt(new Date());
        shop.setStatus(DataStatus.OK.getName());

        long shopId = shopMapper.insert(shop);
        shop.setId(shopId);
        return shop;

    }

    public Shop updateShop(Shop shop, Long userId) {
        Shop shopInDatabase = shopMapper.selectByPrimaryKey(shop.getId());
        if (shopInDatabase == null) {
            throw HttpException.notFound("店铺未找到!");
        }
        if (!Objects.equals(shopInDatabase.getOwnerUserId(), userId)) {
            throw HttpException.forbidden("无权访问");
        }

        shop.setUpdatedAt(new Date());
        shopMapper.updateByPrimaryKey(shop);
        return shop;
    }

    public Shop deleteShop(Long shopId, Long userId) {
        Shop shopInDatabase = shopMapper.selectByPrimaryKey(shopId);
        if (shopInDatabase == null) {
            throw HttpException.notFound("店铺未找到！");
        }

        if (!Objects.equals(shopInDatabase.getOwnerUserId(), userId)) {
            throw HttpException.forbidden("无权访问！");
        }
        shopInDatabase.setStatus(DataStatus.DELETED.getName());
        shopInDatabase.setUpdatedAt(new Date());
        shopMapper.updateByPrimaryKey(shopInDatabase);
        return shopInDatabase;
    }

    public Shop getShopById(long shopId) {
        ShopExample okStatus = new ShopExample();
        okStatus.createCriteria()
                .andIdEqualTo(shopId)
                .andStatusEqualTo(DataStatus.OK.name());
        List<Shop> shops = shopMapper.selectByExample(okStatus);
        if (shops.isEmpty()) {
            throw HttpException.notFound("店铺未找到：" + shopId);
        }
        return shops.get(0);
    }

}

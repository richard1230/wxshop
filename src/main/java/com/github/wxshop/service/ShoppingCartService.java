package com.github.wxshop.service;

import com.github.wxshop.dao.ShoppingCartQueryMapper;
import com.github.wxshop.entity.PageResponse;
import com.github.wxshop.entity.ShoppingCartData;
import com.github.wxshop.entity.ShoppingCartGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class ShoppingCartService {
    private ShoppingCartQueryMapper shoppingCartQueryMapper;

    @Autowired
    public ShoppingCartService(ShoppingCartQueryMapper shoppingCartQueryMapper) {
        this.shoppingCartQueryMapper = shoppingCartQueryMapper;
    }

    public PageResponse<ShoppingCartData> getShoppingCartOfUser(Long userId,
                                                                int pageNum,
                                                                int pageSize) {
        //购物车里面的东西 按照店铺分页
        int offset = (pageNum - 1) * pageSize;
        int totalNum = shoppingCartQueryMapper.countHowManyShopsInUserShoppingCart(userId);
        List<ShoppingCartData> pagedData = shoppingCartQueryMapper.selectShoppingCartDataByUserId(userId, pageSize, offset)
                .stream()
                .collect(groupingBy(shoppingCartData -> shoppingCartData.getShop().getId()))
                .values()
                .stream()
                .map(this::merge)
                .collect(toList());

        int totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        return PageResponse.pagedData(pageNum, pageSize, totalPage, pagedData);
    }

    private ShoppingCartData merge(List<ShoppingCartData> goodsOfSameShop) {
        ShoppingCartData result = new ShoppingCartData();
        result.setShop(goodsOfSameShop.get(0).getShop());
        List<ShoppingCartGoods> goods = goodsOfSameShop.stream()
                .map(ShoppingCartData::getGoods)
                .flatMap(List::stream)
                .collect(toList());
        result.setGoods(goods);
        return result;
    }
}

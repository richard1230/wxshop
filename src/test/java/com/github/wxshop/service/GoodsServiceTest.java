package com.github.wxshop.service;


import com.github.wxshop.entity.DataStatus;
import com.github.wxshop.entity.HttpException;
import com.github.wxshop.entity.PageResponse;
import com.github.wxshop.generate.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class GoodsServiceTest {

    @Mock
    private GoodsMapper goodsMapper;

    @Mock
    private ShopMapper shopMapper;

    @Mock
    private Shop shop;

    @Mock
    private Goods goods;

    //GoodsService将依赖于上面两个对象goodsMapper和shopMapper
    @InjectMocks
    private GoodsService goodsService;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(1L);
        UserContext.setCurrentUser(user);

        //lenient 宽容的
        Mockito.lenient().when(shopMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(shop);
    }

    @AfterEach
    public void clearUserContext() {
        UserContext.setCurrentUser(null);
    }


    //写单元测试的时候,需要注意此时是没有数据库的
    @Test
    public void createGoodsSucceedIfUserIsOwner() {
        //当调用selectByPrimaryKey方法的时候应该返回一个模拟的东西
//        Mockito.when(shopMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(shop);
        Mockito.when(shop.getOwnerUserId()).thenReturn(1L);
        Mockito.when(goodsMapper.insert(goods)).thenReturn(123);

        Assertions.assertEquals(goods, goodsService.createGoods(goods));
        Mockito.verify(goods).setId(123L);

    }

    @Test
    public void createGoodsFailedIfUserIsNotOwner() {

        //上面是1为正确的，这里写2为不正确的
        Mockito.when(shop.getOwnerUserId()).thenReturn(2L);

        HttpException throwException = Assertions.assertThrows(HttpException.class, () -> {
            goodsService.createGoods(goods);
        });

        Assertions.assertEquals(403, throwException.getStatusCode());
    }

    @Test
    void throwExceptionIfGoodsNotFound() {
        long goodsToBeDeleted = 123;

        Mockito.when(shop.getOwnerUserId()).thenReturn(1L);
        Mockito.when(goodsMapper.selectByPrimaryKey(goodsToBeDeleted)).thenReturn(null);
        HttpException thrownException = Assertions.assertThrows(HttpException.class, () -> {
            goodsService.deleteGoodsById(goodsToBeDeleted);
        });

        Assertions.assertEquals(404, thrownException.getStatusCode());

    }


    @Test
    void deleteGoodsThrowExceptionIfUserIsNotOwner() {
        long goodsToBeDeleted = 123;
        //类比 createGoodsFailedIfUserIsNotOwner
        Mockito.when(shop.getOwnerUserId()).thenReturn(2L);
        //下面这一行有的话会报错,多余
//        Mockito.when(goodsMapper.selectByPrimaryKey(goodsToBeDeleted)).thenReturn(goods);
        HttpException thrownException = Assertions.assertThrows(HttpException.class, () -> {
            goodsService.deleteGoodsById(goodsToBeDeleted);
        });

        Assertions.assertEquals(403, thrownException.getStatusCode());

    }

    @Test
    void deleteGoodsSucceed() {
        long goodsToBeDeleted = 123;

        Mockito.when(shop.getOwnerUserId()).thenReturn(1L);
        Mockito.when(goodsMapper.selectByPrimaryKey(goodsToBeDeleted)).thenReturn(goods);

        goodsService.deleteGoodsById(goodsToBeDeleted);

        Mockito.verify(goods).setStatus(DataStatus.DELETED.getName());

    }

    @Test
    public void getGoodsSucceedWithNullShopId() {
        int pageNumber = 5;
        int pageSize = 10;

        //假设返回55
        Mockito.when(goodsMapper.countByExample(any())).thenReturn(55L);
        PageResponse<Goods> result = goodsService.getGoods(pageNumber, pageSize, null);
        Assertions.assertEquals(6, result.getTotalPage());
        Assertions.assertEquals(5, result.getPageNum());
        Assertions.assertEquals(10, result.getPageSize());

        List mockData = Mockito.mock(List.class);
//        Mockito.when(goodsMapper.selectByExample(any())).thenReturn(mockData);
//        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    public void getGoodsSucceedWithNonNullShopId() {
        int pageNumber = 5;
        int pageSize = 10;

        Mockito.when(goodsMapper.countByExample(any())).thenReturn(100L);
        PageResponse<Goods> result = goodsService.getGoods(pageNumber, pageSize, 456);
        Assertions.assertEquals(10, result.getTotalPage());
        Assertions.assertEquals(5, result.getPageNum());
        Assertions.assertEquals(10, result.getPageSize());

        List mockData = Mockito.mock(List.class);
//        Mockito.when(goodsMapper.selectByExample(any())).thenReturn(mockData);
//        Assertions.assertEquals(mockData, result.getData());
    }


    @Test
    public void updateGoodsSucceed() {
        //为什么要用上面两行,光有第三行不就ok了？
        Mockito.when(shop.getOwnerUserId()).thenReturn(1L);
        Mockito.when(goodsMapper.updateByExample(any(), any())).thenReturn(1);
        Assertions.assertEquals(goods, goodsService.updateGoods(goods));
    }
}

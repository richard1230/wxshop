package com.github.wxshop.service;

import com.github.api.DataStatus;
import com.github.api.data.PageResponse;
import com.github.api.exceptions.HttpException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    // GoodsService将依赖于上面两个对象goodsMapper和shopMapper
    @InjectMocks
    private GoodsService goodsService;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(1L);
        UserContext.setCurrentUser(user);

        // lenient 宽容的
        Mockito.lenient().when(shopMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(shop);
    }

    @AfterEach
    public void clearUserContext() {
        UserContext.setCurrentUser(null);
    }

    @Test
    public void createGoodsSucceedIfUserIsOwner() {
        // 当调用selectByPrimaryKey方法的时候应该返回一个模拟的东西
        Mockito.when(shop.getOwnerUserId()).thenReturn(1L);
        Mockito.when(goodsMapper.insert(goods)).thenReturn(123);

        Assertions.assertEquals(goods, goodsService.createGoods(goods));
        Mockito.verify(goods).setId(123L);
    }

    @Test
    public void createGoodsFailedIfUserIsNotOwner() {

        Mockito.when(shop.getOwnerUserId()).thenReturn(2L);

        HttpException throwException =
                Assertions.assertThrows(
                        HttpException.class,
                        () -> {
                            goodsService.createGoods(goods);
                        });

        Assertions.assertEquals(403, throwException.getStatusCode());
    }

    @Test
    void throwExceptionIfGoodsNotFound() {
        long goodsToBeDeleted = 123;

        Mockito.when(goodsMapper.selectByPrimaryKey(goodsToBeDeleted)).thenReturn(null);
        HttpException thrownException =
                Assertions.assertThrows(
                        HttpException.class,
                        () -> {
                            goodsService.deleteGoodsById(goodsToBeDeleted);
                        });

        Assertions.assertEquals(404, thrownException.getStatusCode());
    }

    @Test
    void deleteGoodsThrowExceptionIfUserIsNotOwner() {
        long goodsToBeDeleted = 123;
        // 类比 createGoodsFailedIfUserIsNotOwner
        Mockito.when(shop.getOwnerUserId()).thenReturn(2L);
        Mockito.when(goodsMapper.selectByPrimaryKey(goodsToBeDeleted)).thenReturn(goods);
        HttpException thrownException =
                Assertions.assertThrows(
                        HttpException.class,
                        () -> {
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

        // 假设返回55
        Mockito.when(goodsMapper.countByExample(any())).thenReturn(55L);
        PageResponse<Goods> result = goodsService.getGoods(pageNumber, pageSize, null);
        Assertions.assertEquals(6, result.getTotalPage());
        Assertions.assertEquals(5, result.getPageNum());
        Assertions.assertEquals(10, result.getPageSize());

        List mockData = Mockito.mock(List.class);
    }

    @Test
    public void getGoodsSucceedWithNonNullShopId() {
        int pageNumber = 5;
        int pageSize = 10;

        List<Goods> mockData = Mockito.mock(List.class);

        when(goodsMapper.countByExample(any())).thenReturn(100L);
        when(goodsMapper.selectByExample(any())).thenReturn(mockData);
        PageResponse<Goods> result = goodsService.getGoods(pageNumber, pageSize, 456L);

        assertEquals(10, result.getTotalPage());
        assertEquals(5, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(mockData, result.getData());
    }
}

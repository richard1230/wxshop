package com.github.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.api.DataStatus;
import com.github.api.data.PageResponse;
import com.github.wxshop.WxshopApplication;
import com.github.wxshop.controller.ShoppingCartController;
import com.github.wxshop.entity.GoodsWithNumber;
import com.github.wxshop.entity.Response;
import com.github.wxshop.entity.ShoppingCartData;
import com.github.wxshop.generate.Goods;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = WxshopApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:test-application.yml"})
public class ShoppingCartIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void canQueryShoppingCartData() throws JsonProcessingException {
        // 执行一个登陆操作
        UserLoginResponse loginResponse = loginAndGetCookie();
        // 第二页,每页大小为1
        PageResponse<ShoppingCartData> response =
                doHttpRequest(
                        "/api/v1/shoppingCart?pageNum=2&pageSize=1", "GET", null, loginResponse.cookie)
                        .asJsonObject(new TypeReference<PageResponse<ShoppingCartData>>() {
                        });
        Assertions.assertEquals(2, response.getPageNum());
        Assertions.assertEquals(1, response.getPageSize());
        Assertions.assertEquals(2, response.getTotalPage());
        // 有一个店铺(下面第一个断言),它的商店的id为2(第二个断言)
        Assertions.assertEquals(1, response.getData().size());
        Assertions.assertEquals(2, response.getData().get(0).getShop().getId());
        // 两件商品的id是4和5
        Assertions.assertEquals(
                Arrays.asList(4L, 5L),
                response.getData().get(0).getGoods().stream().map(Goods::getId).collect(toList()));
        // 两件商品的价格是100和200
        Assertions.assertEquals(
                Arrays.asList(100L, 200L),
                response.getData().get(0).getGoods().stream()
                        .map(GoodsWithNumber::getPrice)
                        .collect(toList()));
        // 两件商品的数量分别是200和300
        Assertions.assertEquals(
                Arrays.asList(200, 300),
                response.getData().get(0).getGoods().stream()
                        .map(GoodsWithNumber::getNumber)
                        .collect(toList()));
    }

    @Test
    public void canAddShoppingCartData() throws Exception {
        UserLoginResponse loginResponse = loginAndGetCookie();

        // post的是AddToShoppingCartRequest这个请求
        ShoppingCartController.AddToShoppingCartRequest request =
                new ShoppingCartController.AddToShoppingCartRequest();
        ShoppingCartController.AddToShoppingCartItem item =
                new ShoppingCartController.AddToShoppingCartItem();
        // 设置所加商品的id
        item.setId(2L);
        // 设置这个商品的数量
        item.setNumber(2);

        request.setGoods(Collections.singletonList(item));

        // 这里的接口是post请求
        Response<ShoppingCartData> response =
                doHttpRequest("/api/v1/shoppingCart", "POST", request, loginResponse.cookie)
                        .asJsonObject(new TypeReference<Response<ShoppingCartData>>() {
                        });

        // 返回的是一号店铺
        Assertions.assertEquals(1L, response.getData().getShop().getId());
        // 一号商店的一号商品和二号商品
        Assertions.assertEquals(
                Arrays.asList(1L, 2L),
                response.getData().getGoods().stream().map(Goods::getId).collect(toList()));
        // 预期商品数量为2件(上面设置的)和100件(见V5__AddShopIdToShoppingCart这个表单里面的一号店铺)
        Assertions.assertEquals(
                Sets.newHashSet(2, 100),
                response.getData().getGoods().stream().map(GoodsWithNumber::getNumber).collect(toSet()));
        // 店铺id是不是1
        Assertions.assertTrue(
                response.getData().getGoods().stream().allMatch(goods -> goods.getShopId() == 1L));
    }

    // 重复将同一个商品加入购物车，后面的商品会覆盖前面的
    @Test
    public void addingSameGoodsToShoppingCartOverwritesOldGoods() throws Exception {
        // 第一次添加id为2的商品，2个
        canAddShoppingCartData();

        UserLoginResponse loginResponse = loginAndGetCookie();

        // 第二次添加id为2的商品，1个
        ShoppingCartController.AddToShoppingCartRequest request =
                new ShoppingCartController.AddToShoppingCartRequest();
        ShoppingCartController.AddToShoppingCartItem item =
                new ShoppingCartController.AddToShoppingCartItem();
        item.setId(2L);
        item.setNumber(1);

        request.setGoods(Collections.singletonList(item));

        Response<ShoppingCartData> addShoppingCartResponse =
                doHttpRequest("/api/v1/shoppingCart", "POST", request, loginResponse.cookie)
                        .asJsonObject(new TypeReference<Response<ShoppingCartData>>() {
                        });

        PageResponse<ShoppingCartData> getShoppingCartResponse =
                doHttpRequest(
                        "/api/v1/shoppingCart?pageNum=1&pageSize=100", "GET", null, loginResponse.cookie)
                        .asJsonObject(new TypeReference<PageResponse<ShoppingCartData>>() {
                        });

        ShoppingCartData shop1Data =
                getShoppingCartResponse.getData().stream()
                        .filter(data -> data.getShop().getId() == 1)
                        .findFirst()
                        .get();

        Assertions.assertEquals(
                Arrays.asList(1L, 2L), shop1Data.getGoods().stream().map(Goods::getId).collect(toList()));
        Assertions.assertEquals(
                Sets.newHashSet(1, 100),
                shop1Data.getGoods().stream().map(GoodsWithNumber::getNumber).collect(toSet()));
    }

    @Test
    public void canDeleteAllShoppingCartData() throws Exception {
        UserLoginResponse loginResponse = loginAndGetCookie();

        List<Long> ids = Arrays.asList(1L, 4L, 5L);
        for (Long id : ids) {
            Response<ShoppingCartData> response =
                    doHttpRequest("/api/v1/shoppingCart/" + id, "DELETE", null, loginResponse.cookie)
                            .assertOkStatusCode()
                            .asJsonObject(new TypeReference<Response<ShoppingCartData>>() {
                            });
        }
        PageResponse<ShoppingCartData> getResponse =
                doHttpRequest(
                        "/api/v1/shoppingCart?pageNum=1&pageSize=10", "GET", null, loginResponse.cookie)
                        .asJsonObject(new TypeReference<PageResponse<ShoppingCartData>>() {
                        });
        Assertions.assertEquals(0, getResponse.getData().size());
    }

    @Test
    public void canDeleteShoppingCartData() throws Exception {
        UserLoginResponse loginResponse = loginAndGetCookie();

        Response<ShoppingCartData> response =
                doHttpRequest("/api/v1/shoppingCart/5", "DELETE", null, loginResponse.cookie)
                        .asJsonObject(new TypeReference<Response<ShoppingCartData>>() {
                        });

        Assertions.assertEquals(2L, response.getData().getShop().getId());

        Assertions.assertEquals(1, response.getData().getGoods().size());
        GoodsWithNumber goods = response.getData().getGoods().get(0);

        Assertions.assertEquals(4L, goods.getId());
        Assertions.assertEquals(200, goods.getNumber());
        Assertions.assertEquals(DataStatus.OK.toString().toLowerCase(), goods.getStatus());
        PageResponse<ShoppingCartData> getResponse =
                doHttpRequest(
                        "/api/v1/shoppingCart?pageNum=1&pageSize=10", "GET", null, loginResponse.cookie)
                        .asJsonObject(new TypeReference<PageResponse<ShoppingCartData>>() {
                        });

        // 两家店铺各有一个
        Assertions.assertEquals(2, getResponse.getData().size());
        Assertions.assertEquals(1, getResponse.getTotalPage());
        Assertions.assertEquals(1, getResponse.getData().get(0).getGoods().size());
        Assertions.assertEquals(1, getResponse.getData().get(1).getGoods().size());
        Assertions.assertEquals(1L, getResponse.getData().get(0).getGoods().get(0).getId());
        Assertions.assertEquals(4L, getResponse.getData().get(1).getGoods().get(0).getId());
    }
}

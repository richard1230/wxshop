package com.github.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.wxshop.WxshopApplication;
import com.github.wxshop.entity.PageResponse;
import com.github.wxshop.entity.ShoppingCartData;
import com.github.wxshop.entity.ShoppingCartGoods;
import com.github.wxshop.generate.Goods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:test-application.yml"})
public class ShoppingCartIntegrationTest extends AbstractIntegrationTest{

    @Test
    public void canQueryShoppingCartData() throws JsonProcessingException {
        //执行一个登陆操作
        UserLoginResponse loginResponse = loginAndGetCookie();

        //第二页,每页大小为1
        PageResponse<ShoppingCartData> response = doHttpRequest("/api/v1/shoppingCart?pageNum=2&pageSize=1",
                "GET", null, loginResponse.cookie).asJsonObject(new TypeReference<PageResponse<ShoppingCartData>>() {
        });

        Assertions.assertEquals(2, response.getPageNum());
        Assertions.assertEquals(1, response.getPageSize());
        Assertions.assertEquals(2, response.getTotalPage());
        //有一个店铺(下面第一个断言),它的商店的id为2(第二个断言)
        Assertions.assertEquals(1, response.getData().size());
        Assertions.assertEquals(2, response.getData().get(0).getShop().getId());
        //两件商品的id是4和5
        Assertions.assertEquals(Arrays.asList(4L, 5L),
                response.getData().get(0).getGoods().stream()
                        .map(Goods::getId).collect(Collectors.toList()));
        //两件商品的价格是100和200
        Assertions.assertEquals(Arrays.asList(100L, 200L),
                response.getData().get(0).getGoods().stream()
                        .map(ShoppingCartGoods::getPrice).collect(Collectors.toList()));
        //两件商品的数量分别是200和300
        Assertions.assertEquals(Arrays.asList(200, 300),
                response.getData().get(0).getGoods().stream()
                        .map(ShoppingCartGoods::getNumber).collect(Collectors.toList()));

    }
}

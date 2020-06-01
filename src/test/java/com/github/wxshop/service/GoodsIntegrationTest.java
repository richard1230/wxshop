package com.github.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.wxshop.WxshopApplication;
import com.github.wxshop.entity.Response;
import com.github.wxshop.generate.Goods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@ExtendWith(SpringExtension.class)   //SpringExtension:spring为junit5提供的插件,你可以在这个测试里面使用spring相关的功能,包括依赖注入
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//在这个测试里面所启动的springboot容器就具有了spring.config.location=classpath属性
@TestPropertySource(properties = {"spring.config.location=classpath:test-application.yml"})
public class GoodsIntegrationTest extends AbstractIntegrationTest{


    @Test
    public void testCreateGoods() throws JsonProcessingException {

        String cookie = loginAndGetCookie();

        Goods goods = new Goods();
        goods.setName("肥皂");
        goods.setDescription("纯天然无污染肥皂");
        goods.setDetails("这是一块好肥皂");
        goods.setImgUrl("http://url");
        goods.setPrice(1000L);  //单位是分
        goods.setStock(10);
        goods.setShopId(1L);

        HttpResponse response = doHttpRequest(
                "/api/v1/goods",
                "POST",
                goods,
                cookie);

        //TypeReference:序列化出的一个带泛型的对象
        Response<Goods> responseData = objectMapper.readValue(response.body, new TypeReference<Response<Goods>>() {});

        Assertions.assertEquals(SC_CREATED,response.code);
        Assertions.assertEquals("肥皂",responseData.getData().getName());

    }

    @Test
    public void return404IfGoodsToDeleteNotExist() throws JsonProcessingException {
        String cookie = loginAndGetCookie();
        HttpResponse response = doHttpRequest(
                "/api/v1/goods/12345678",
                "DELETE",
                null,
                cookie);
        Assertions.assertEquals(SC_NOT_FOUND, response.code);

    }
}

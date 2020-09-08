package com.github.wxshop.generate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class GoodsTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serializationDeserializationTest() throws JsonProcessingException {
        Goods goods = new Goods();
        goods.setName("肥皂");
        goods.setDescription("纯天然无污染肥皂");
        goods.setDetails("这是一块好肥皂");
        goods.setImgUrl("http://url");
        goods.setPrice(1000L);
        goods.setStock(10);
        goods.setShopId(1L);

        // writeValueAsString : goods序列化成 字符串
        String value = objectMapper.writeValueAsString(goods);
        // readValue:deserialize
        objectMapper.readValue(value, Goods.class);
    }
}

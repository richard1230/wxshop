package com.github.wxshop.service;

import com.github.wxshop.WxshopApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)//SpringExtension:spring为junit5提供的插件,你可以在这个测试里面使用spring相关的功能,包括依赖注入
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(locations = "classpath:application.yml")
public class GoodsIntegrationTest {
}

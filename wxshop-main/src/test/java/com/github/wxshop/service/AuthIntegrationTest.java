package com.github.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.wxshop.WxshopApplication;
import com.github.wxshop.entity.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.TestPropertySource;
import static com.github.wxshop.service.TelVerificationServiceTest.VALID_PARAMTER;
import static com.github.wxshop.service.TelVerificationServiceTest.WRONG_CODE;
import static java.net.HttpURLConnection.*;

@ExtendWith(SpringExtension.class)//SpringExtension:spring为junit5提供的插件,你可以在这个测试里面使用spring相关的功能,包括依赖注入
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//选择随机端口(原来的8080端口可能被占或者需要测并发时候的情况),端口可以在下面的environment中找到
@TestPropertySource(properties = {"spring.config.location=classpath:test-application.yml"})
public class  AuthIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void loginLogoutTest() throws JsonProcessingException {

        String sessionId = loginAndGetCookie().cookie;

        //4.带着cookie访问,/api/v1/status处于登陆状态
        String statusResponse = doHttpRequest("/api/v1/status", "GET", null, sessionId).body;
        //带着Cookies访问就是登陆状态
        LoginResponse response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertTrue(response.isLogin());
        Assertions.assertEquals(VALID_PARAMTER.getTel(), response.getUser().getTel());

        //注销登陆,注意注销登陆也需要带cookie
        //5.调用/api/logout
        doHttpRequest("/api/v1/logout", "POST", null, sessionId);
        //恢复成未登录状态
        //6.再次带着Cookie访问/api/status/恢复称为未登录状态
        statusResponse = doHttpRequest("/api/v1/status", "GET", null, sessionId).body;

        response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());
    }

    @Test
    public void returnHttpOKWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/v1/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsBytes(VALID_PARAMTER))
                .code();
        Assertions.assertEquals(HTTP_OK, responseCode);
    }

    @Test
    public void returnForbiddenWhenCodeIsNotCorrect() throws Exception {

        int responseCode = doHttpRequest(
                "/api/v1/login",
                "POST",
                WRONG_CODE,
                null).code;
        Assertions.assertEquals(HTTP_FORBIDDEN,responseCode);
    }


    @Test
    public void returnHttpBadRequestWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/v1/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsBytes(TelVerificationServiceTest.EMPTY_TEL))
                .code();
        Assertions.assertEquals(HTTP_BAD_REQUEST, responseCode);
    }
}

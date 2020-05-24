package com.github.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.wxshop.WxshopApplication;
import com.github.wxshop.entity.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static com.github.wxshop.service.TelVerificationServiceTest.VALID_PARAMTER;
import static com.github.wxshop.service.TelVerificationServiceTest.VALID_PARAMTER_CODE;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

@ExtendWith(SpringExtension.class)//SpringExtension:spring为junit5提供的插件,你可以在这个测试里面使用spring相关的功能,包括依赖注入
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//选择随机端口(原来的8080端口可能被占或者需要测并发时候的情况),端口可以在下面的environment中找到
@TestPropertySource(locations = "classpath:application.yml")
public class AuthIntegrationTest {
    @Autowired
    Environment environment;

    private ObjectMapper objectMapper = new ObjectMapper();

    public static class HttpResponse {
        int code;
        String body;
        Map<String, List<String>> headers;

        public HttpResponse(int code, String body, Map<String, List<String>> headers) {
            this.code = code;
            this.body = body;
            this.headers = headers;
        }
    }

    private HttpResponse doHttpRequest(String apiName, boolean isGet, Object requestBody, String cookie) throws JsonProcessingException {
        HttpRequest request = isGet ? HttpRequest.get(getUrl(apiName)) : HttpRequest.post(getUrl(apiName));
        if (cookie != null) {
            request.header("Cookie", cookie);
        }
        request.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);

        if (requestBody != null) {
            request.send(objectMapper.writeValueAsString(requestBody));
        }

        return new HttpResponse(request.code(), request.body(), request.headers());
    }

    @Test
    public void loginLogoutTest() throws JsonProcessingException {

        //1.最开始默认情况,访问/api/status/处于未登录状态
        String statusResponse = doHttpRequest("/api/status", true, null, null).body;
        //没带cookies访问就是未登录状态
        LoginResponse response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());

        //2.发送验证码
        int responseCode = doHttpRequest("/api/code", false, VALID_PARAMTER, null).code;
        Assertions.assertEquals(HTTP_OK, responseCode);

        //3.带着验证码进行登录,得到cookie
        Map<String, List<String>> responseHeaders = doHttpRequest("/api/login", false, VALID_PARAMTER_CODE, null).headers;

        List<String> setCookie = responseHeaders.get("Set-Cookie");

        String sessionId = setCookie.stream().filter(cookie -> cookie.contains("JSESSIONID"))
                .findFirst()
                .get();

        //4.带着cookie访问,/api/status处于登陆状态
        statusResponse = doHttpRequest("/api/status", true, null, sessionId).body;
        //带着Cookies访问就是登陆状态
        response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertTrue(response.isLogin());
        Assertions.assertEquals(VALID_PARAMTER.getTel(), response.getUser().getTel());

        //注销登陆,注意注销登陆也需要带cookie
        //5.调用/api/logout
        doHttpRequest("/api/logout", false, null, sessionId);

        //恢复成未登录状态
        //6.再次带着Cookie访问/api/status/恢复称为未登录状态
        statusResponse = doHttpRequest("/api/status", true, null, sessionId).body;

        response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());

    }


    private String getSessionIdFromSetCookie(String setCookie) {
        // JSESSIONID=7c647c1a-373e-49e4-9412-9bd998bcfa1b; Path=/; HttpOnly; SameSite=lax -> JSESSIONID=7c647c1a-373e-49e4-9412-9bd998bcfa1b
        int semiColonIndex = setCookie.indexOf(";");
        return setCookie.substring(0, semiColonIndex);
    }


    @Test
    public void returnHttpOKWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsBytes(VALID_PARAMTER))
                .code();
        Assertions.assertEquals(HTTP_OK, responseCode);
    }

    @Test
    public void returnHttpBadRequestWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsBytes(TelVerificationServiceTest.EMPTY_TEL))
                .code();
        Assertions.assertEquals(HTTP_BAD_REQUEST, responseCode);
    }

    private String getUrl(String apiName) {
        // 获取集成测试的端口号
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }
}

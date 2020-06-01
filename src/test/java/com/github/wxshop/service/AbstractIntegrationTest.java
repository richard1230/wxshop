package com.github.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.wxshop.entity.LoginResponse;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Map;

import static com.github.wxshop.service.TelVerificationServiceTest.VALID_PARAMTER;
import static com.github.wxshop.service.TelVerificationServiceTest.VALID_PARAMTER_CODE;
import static java.net.HttpURLConnection.HTTP_OK;

public class AbstractIntegrationTest {

    @Autowired
    Environment environment;

    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    @Value("${spring.datasource.password}")
    private String databasePassword;

    @BeforeEach
    public void initDatabase() {
        //在每个测试开始之前，执行一个flyway:clean flyway:migrate
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(databaseUrl, databaseUsername, databasePassword);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }

    public ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
    }

    public String getUrl(String apiName) {
        //获取集成测试的端口号
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }

    public String loginAndGetCookie() throws JsonProcessingException {
        //1.最开始默认情况,访问/api/status/处于未登录状态
        String statusResponse = doHttpRequest("/api/v1/status", "GET", null, null).body;
        LoginResponse response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());

        //2.发送验证码
        int responseCode = doHttpRequest("/api/v1/code", "POST", VALID_PARAMTER, null).code;
        Assertions.assertEquals(HTTP_OK, responseCode);

        // 带着验证码进行登录，得到Cookie
        Map<String, List<String>> responseHeaders = doHttpRequest("/api/v1/login", "POST", VALID_PARAMTER_CODE, null).headers;
        List<String> setCookie = responseHeaders.get("Set-Cookie");

        return getSessionIdFromSetCookie(setCookie.stream().filter(cookie -> cookie.contains("JSESSIONID"))
                .findFirst()
                .get());

    }

    String getSessionIdFromSetCookie(String setCookie) {
        int semiColonIndex = setCookie.indexOf(";");
        return setCookie.substring(0, semiColonIndex);
    }

    public static class HttpResponse {
        int code;
        String body;
        Map<String, List<String>> headers;

        HttpResponse(int code, String body, Map<String, List<String>> headers) {
            this.code = code;
            this.body = body;
            this.headers = headers;
        }
    }

     HttpResponse doHttpRequest(String apiName, String httpMethod, Object requestBody, String cookie) throws JsonProcessingException {
        HttpRequest request = new HttpRequest(getUrl(apiName), httpMethod);
        if (cookie != null) {
            request.header("Cookie", cookie);
        }
        request.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);

        if (requestBody != null) {
            request.send(objectMapper.writeValueAsString(requestBody));
        }

        return new HttpResponse(request.code(), request.body(), request.headers());
    }
}

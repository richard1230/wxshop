package com.github.wxshop.controller;

import com.github.api.rpc.OrderRpcService;
import com.github.wxshop.entity.LoginResponse;
import com.github.wxshop.service.AuthService;
import com.github.wxshop.service.TelVerificationService;
import com.github.wxshop.service.UserContext;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService authService;
    private final TelVerificationService telVerificationService;

    @Autowired
    public AuthController(AuthService authService, TelVerificationService telVerificationService) {
        this.authService = authService;
        this.telVerificationService = telVerificationService;
    }

    /**
     * @api {post} /code 请求验证码
     * @apiName GetCode
     * @apiGroup 登录与鉴权
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiParam {String} tel 手机号码
     * @apiParamExample {json} Request-Example: { "tel": "13812345678", }
     * @apiSuccessExample Success-Response: HTTP/1.1 200 OK
     * @apiError 400 Bad Request 若用户的请求包含错误
     * @apiErrorExample Error-Response: HTTP/1.1 400 Bad Request { "message": "Bad Request" }
     */
    /**
     * @param telAndCode 手机号和收到的验证码
     * @param response   HTTP response
     */
    @PostMapping("/code")
    public void code(@RequestBody TelAndCode telAndCode, HttpServletResponse response) {
        if (telVerificationService.verifyTelParameter(telAndCode)) {
            authService.sendVerificationCode(telAndCode.getTel());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    /**
     * @api {post} /login 登录
     * @apiName Login
     * @apiGroup 登录与鉴权
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiParam {String} tel 手机号码
     * @apiParam {String} code 验证码
     * @apiParamExample {json} Request-Example: { "tel": "13812345678", "code": "000000" }
     * @apiSuccessExample Success-Response: HTTP/1.1 200 OK
     * @apiError 400 Bad Request 若用户的请求包含错误
     * @apiError 403 Forbidden 若用户的验证码错误
     * @apiErrorExample Error-Response: HTTP/1.1 400 Bad Request { "message": "Bad Request" }
     */
    /**
     * @param telAndCode 手机号
     * @param response   HTTP响应
     */
    @PostMapping("/login")
    public void login(@RequestBody TelAndCode telAndCode, HttpServletResponse response) {
        UsernamePasswordToken token =
                new UsernamePasswordToken(telAndCode.getTel(), telAndCode.getCode());

        token.setRememberMe(true); // 这一步可能有问题！！
        try {
            SecurityUtils.getSubject().login(token);
        } catch (IncorrectCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * @api {post} /logout 登出
     * @apiName Logout
     * @apiGroup 登录与鉴权
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiSuccessExample Success-Response: HTTP/1.1 200 OK
     * @apiError 401 Unauthorized 若用户未登录
     * @apiErrorExample Error-Response: HTTP/1.1 400 Bad Request { "message": "Bad Request" }
     */
    @PostMapping("/logout")
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * @api {get} /status 获取登录状态
     * @apiName Status
     * @apiGroup 登录与鉴权
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiSuccess {User} user 用户信息
     * @apiSuccess {Boolean} login 登录状态
     * @apiSuccessExample Success-Response: HTTP/1.1 200 OK { "login": true, "user": { "id": 123,
     * "name": "张三", "tel": "13812345678", "avatarUrl": "https://url", "address": "北京市 西城区 100号",
     * } }
     * @apiError 401 Unauthorized 若用户未登录
     * @apiErrorExample Error-Response: HTTP/1.1 401 Unauthorized { "message": "Unauthorized" }
     */
    @Reference(version = "${wxshop.orderservice.version}")
    OrderRpcService orderService;

    /**
     * @return 登录状态
     */
    @GetMapping("/status")
    public Object loginStatus() {
        if (UserContext.getCurrentUser() == null) {
            return LoginResponse.notLogin();
        } else {
            return LoginResponse.login(UserContext.getCurrentUser());
        }
    }

    public static class TelAndCode {
        private String tel;
        private String code;

        public TelAndCode(String tel, String code) {
            this.tel = tel;
            this.code = code;
        }

        // 手机号即用户名
        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        // code即密码
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}

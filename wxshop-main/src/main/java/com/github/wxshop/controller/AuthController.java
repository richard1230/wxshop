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
    public AuthController(AuthService authService,
                          TelVerificationService telVerificationService) {
        this.authService = authService;
        this.telVerificationService = telVerificationService;
    }

    @PostMapping("/code")
    public void code(@RequestBody TelAndCode telAndCode,
                     HttpServletResponse response
    ) {
        if (telVerificationService.verifyTelParameter(telAndCode)) {
            authService.sendVerificationCode(telAndCode.getTel());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

    }



    @PostMapping("/login")
    public void login(@RequestBody TelAndCode telAndCode, HttpServletResponse response){
        UsernamePasswordToken token = new UsernamePasswordToken(
                telAndCode.getTel(),
                telAndCode.getCode());

        token.setRememberMe(true ); //这一步可能有问题！！
        try {
            SecurityUtils.getSubject().login(token);
        } catch (IncorrectCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(){
        SecurityUtils.getSubject().logout();
    }

    @Reference(version = "${wxshop.orderservice.version}")
    OrderRpcService orderService;

    @GetMapping("/status")
    public Object loginStatus(){
        if (UserContext.getCurrentUser()==null){
            return LoginResponse.notLogin();
        }else {
            return LoginResponse.login(UserContext.getCurrentUser());
        }
    }

    public static class TelAndCode{
        private String tel;
        private String code;

        public TelAndCode(String tel, String code) {
            this.tel = tel;
            this.code = code;
        }

        //手机号即用户名
        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        //code即密码
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}

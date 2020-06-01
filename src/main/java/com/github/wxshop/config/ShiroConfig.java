package com.github.wxshop.config;


import com.github.wxshop.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig implements WebMvcConfigurer {

    @Autowired
    UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                Object tel = SecurityUtils.getSubject().getPrincipal();
                if (tel != null) {
                    userService.getUserByTel(tel.toString()).ifPresent(UserContext::setCurrentUser);
                }
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                UserContext.clearCurrentUser();
            }
        });
    }



    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroLoginFilter shiroLoginFilter) {//这里爆红是因为这里是注入,这种行为是运行时行为 ,IDEA对于这种检测不是100%准确
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> pattern = new HashMap<>();
        pattern.put("/api/v1/code", "anon");       //如果用户未登录,除了这四个接口,访问其他接口都会返回404
        pattern.put("/api/v1/login", "anon");      //这里为匿名接口,不需要过滤器
        pattern.put("/api/v1/status", "anon");
        pattern.put("/api/v1/logout", "anon");
        pattern.put("/**", "authc");          //非匿名接口需要进入过滤器shiroLoginFilter,没有登录会被拒绝

        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        filtersMap.put("shiroLoginFilter", shiroLoginFilter);
        shiroFilterFactoryBean.setFilters(filtersMap);


        shiroFilterFactoryBean.setFilterChainDefinitionMap(pattern);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(shiroRealm);
        securityManager.setCacheManager(new MemoryConstrainedCacheManager()); //内存受限制的缓存管理
        securityManager.setSessionManager(new DefaultWebSessionManager());
        SecurityUtils.setSecurityManager(securityManager);

        return securityManager;

    }

    @Bean
    public ShiroRealm myShiroRealm(VerificationCodeCheckService verificationCodeCheckService) {
        return new ShiroRealm(verificationCodeCheckService);
    }
}


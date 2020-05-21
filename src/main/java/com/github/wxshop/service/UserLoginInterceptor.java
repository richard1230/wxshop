package com.github.wxshop.service;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {

    private UserService userService;

    public UserLoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object tel = SecurityUtils.getSubject().getPrincipal();
        if (tel != null) {
            //说明已经登录了
                User user = userService.getUserByTel(tel.toString()); //将用户数据读取出来
                UserContext.setCurrentUser(user);                     //将数据放到上下文里面,后面所有的相同线程发生的control和services都可以直接拿来用
//            userService.getUserByTel(tel.toString()).ifPresent(UserContext::setCurrentUser);

        }
        System.out.println("Pre!");
        return true;    //返回false会直接将方法拦截掉
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //very import,thread can be reused!!!线程可以复用
        //如果在线程1里面保存了用户A的信息,并且没有清理的话
        //下次线程1再次用来处理别的请求的时候,就会出现串号的情况;
        UserContext.setCurrentUser(null);
    }
}

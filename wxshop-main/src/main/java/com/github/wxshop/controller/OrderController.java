package com.github.wxshop.controller;


import com.github.api.rpc.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
//    @Reference(version = "${wxshop.orderservice.version}")
    @Autowired
    OrderService orderService; //它所对应的包在pom所指定的依赖里面
//
//    @RequestMapping("/testRpc")
//    public String testRpc(){
//      String test =   orderService.sayHello("richard");
//        return test;
//    }


    public void getOrder() {
    }

    public void createOrder() {
    }

    public void updateOrder() {
    }


    public void deleteOrder() {
    }

}

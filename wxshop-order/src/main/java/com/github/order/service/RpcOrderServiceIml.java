package com.github.order.service;

import com.github.api.rpc.OrderService;
import org.apache.dubbo.config.annotation.Service;

@Service(version = "${wxshop.orderservice.version}")
public class RpcOrderServiceIml implements OrderService {

    @Override
    public String sayHello(String name) {
        return "hello, "+name;
    }
}

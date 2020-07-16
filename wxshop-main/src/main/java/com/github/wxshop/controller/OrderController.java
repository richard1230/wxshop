package com.github.wxshop.controller;


import com.github.api.data.OrderInfo;
import com.github.wxshop.entity.OrderResponse;
import com.github.wxshop.entity.Response;
import com.github.wxshop.service.OrderService;
import com.github.wxshop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class OrderController {
    OrderService orderService; //它所对应的包在pom所指定的依赖里面

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    public void getOrder() {
    }

    @PostMapping("/order")
    public Response<OrderResponse> createOrder(@RequestBody OrderInfo orderInfo) {
        orderService.deductStock(orderInfo);
        return Response.of(orderService.createOrder(orderInfo, UserContext.getCurrentUser().getId()));
    }

    public void updateOrder() {
    }


    public void deleteOrder() {
    }

}

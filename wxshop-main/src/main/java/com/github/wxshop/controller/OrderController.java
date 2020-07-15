package com.github.wxshop.controller;


import com.github.api.data.OrderInfo;
import com.github.wxshop.entity.HttpException;
import com.github.wxshop.entity.OrderResponse;
import com.github.wxshop.entity.Response;
import com.github.wxshop.service.OrderService;
import com.github.wxshop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class  OrderController {
    OrderService orderService; //它所对应的包在pom所指定的依赖里面

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired


    public void getOrder() {
    }

    @PostMapping("/order")
    public Response<OrderResponse> createOrder(@RequestBody OrderInfo orderInfo,
                                               HttpServletResponse response )
    {
        try{
            orderService.deductStock(orderInfo);
            return Response.of(orderService.createOrder(orderInfo, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.of(e.getMessage(),null);
        }
    }

    public void updateOrder() {
    }


    public void deleteOrder() {
    }

}

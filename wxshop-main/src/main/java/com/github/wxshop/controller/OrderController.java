package com.github.wxshop.controller;


import com.github.api.DataStatus;
import com.github.api.data.OrderInfo;
import com.github.api.data.PageResponse;
import com.github.api.exceptions.HttpException;
import com.github.api.generate.Order;
import com.github.wxshop.entity.OrderResponse;
import com.github.wxshop.entity.Response;
import com.github.wxshop.service.OrderService;
import com.github.wxshop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class OrderController {
    OrderService orderService; //它所对应的包在pom所指定的依赖里面

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @GetMapping("/order")
    public PageResponse<OrderResponse> getOrder(@RequestParam("pageNum") Integer pageNum,
                                                @RequestParam("pageSize") Integer pageSize,
                                                @RequestParam(value = "status",required = false) String status

    ) {
        if (status != null && DataStatus.fromStatus(status) == null){
            throw HttpException.badRequest("非法status: " + status);
        }
        return orderService.getOrder(UserContext.getCurrentUser().getId(),pageNum,pageSize,DataStatus.fromStatus(status));
    }

    @PostMapping("/order")
    public Response<OrderResponse> createOrder(@RequestBody OrderInfo orderInfo) {
        orderService.deductStock(orderInfo);
        return Response.of(orderService.createOrder(orderInfo, UserContext.getCurrentUser().getId()));
    }

    @RequestMapping(value = "/order/{id}", method = {RequestMethod.POST, RequestMethod.PATCH})
    public Response<OrderResponse> updateOrder(@PathVariable("id") Integer id, @RequestBody Order order) {
        if (order.getExpressCompany() != null) {
            return Response.of(orderService.updateExpressInformation(order, UserContext.getCurrentUser().getId()));
        } else {
            return Response.of(orderService.updateOrderStatus(order, UserContext.getCurrentUser().getId()));
        }
    }

    @DeleteMapping("/order/{id}")
    public Response<OrderResponse> deleteOrder(@PathVariable("id") long orderId) {
        return Response.of(orderService.deleteOrder(orderId,UserContext.getCurrentUser().getId()));
    }

}

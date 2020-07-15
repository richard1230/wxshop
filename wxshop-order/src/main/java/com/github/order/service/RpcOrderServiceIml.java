package com.github.order.service;

import com.github.api.DataStatus;
import com.github.api.data.OrderInfo;
import com.github.api.data.PageResponse;
import com.github.api.data.RpcOrderGoods;
import com.github.api.generate.Order;
import com.github.api.rpc.OrderRpcService;
import com.github.order.generate.OrderMapper;
import com.github.order.mapper.MyOrderMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.function.BooleanSupplier;

@Service(version = "${wxshop.orderservice.version}")
public class RpcOrderServiceIml implements OrderRpcService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MyOrderMapper myOrderMapper;

    @Override
    public Order createOrder(OrderInfo orderInfo, Order order) {
        insertOrder(order);
        myOrderMapper.insertOrders(orderInfo);
        return order;
    }

    private void insertOrder(Order order) {
        order.setStatus(DataStatus.PENDING.getName());
        // 将原来重复的if-else变成了下面的函数式编程，提取了一个公用方法verify
        verify(()->order.getUserId()==null,"userId不能为空");
        verify(()->order.getTotalPrice()==null || order.getTotalPrice().doubleValue() < 0,"totalPrice非法！");
        verify(()->order.getAddress()==null,"address不能为空");

        order.setExpressCompany(null);
        order.setExpressId(null);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        long id = orderMapper.insert(order);
        order.setId(id);
    }

    private void verify(BooleanSupplier supplier,String message) {
        if (supplier.getAsBoolean()){
            throw new IllegalArgumentException(message);
        }
    }

//    @Override
//    public RpcOrderGoods getOrderById(long orderId) {
//        return null;
//    }
//
//    @Override
//    public RpcOrderGoods deleteOrder(long orderId, long userId) {
//        return null;
//    }
//
//    @Override
//    public PageResponse<RpcOrderGoods> getOrder(long userId, Integer pageNum, Integer pageSize, DataStatus status) {
//        return null;
//    }
//
//    @Override
//    public RpcOrderGoods updateOrder(Order order) {
//        return null;
//    }
}

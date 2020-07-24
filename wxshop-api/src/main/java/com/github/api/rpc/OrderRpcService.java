package com.github.api.rpc;

import com.github.api.DataStatus;
import com.github.api.data.OrderInfo;
import com.github.api.data.PageResponse;
import com.github.api.data.RpcOrderGoods;
import com.github.api.generate.Order;

public interface OrderRpcService {
    Order createOrder(OrderInfo orderInfo, Order order);
    RpcOrderGoods getOrderById(long orderId);
    RpcOrderGoods deleteOrder(long orderId, long userId);
    PageResponse<RpcOrderGoods> getOrder(long userId, Integer pageNum, Integer pageSize, DataStatus status);

    RpcOrderGoods updateOrder(Order order);
}

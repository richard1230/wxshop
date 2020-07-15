package com.github.api.rpc;

import com.github.api.data.OrderInfo;
import com.github.api.generate.Order;

public interface OrderRpcService {
    Order createOrder(OrderInfo orderInfo, Order order);
}

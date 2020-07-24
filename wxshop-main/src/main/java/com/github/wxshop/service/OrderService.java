package com.github.wxshop.service;

import com.github.api.DataStatus;
import com.github.api.data.GoodsInfo;
import com.github.api.data.OrderInfo;
import com.github.api.data.PageResponse;
import com.github.api.data.RpcOrderGoods;
import com.github.api.generate.Order;
import com.github.api.rpc.OrderRpcService;
import com.github.wxshop.dao.GoodsStockMapper;
import com.github.wxshop.entity.GoodsWithNumber;
import com.github.api.exceptions.HttpException;
import com.github.wxshop.entity.OrderResponse;
import com.github.wxshop.generate.Goods;
import com.github.wxshop.generate.Shop;
import com.github.wxshop.generate.ShopMapper;
import com.github.wxshop.generate.UserMapper;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Reference(version = "${wxshop.orderservice.version}")
    private OrderRpcService orderRpcService;

    private UserMapper userMapper;

    private GoodsStockMapper goodsStockMapper;

    private GoodsService goodsService;

    private ShopMapper shopMapper;


    @Autowired
    public OrderService(UserMapper userMapper,
                        GoodsStockMapper goodsStockMapper,
                        GoodsService goodsService,
                        ShopMapper shopMapper) {
        this.userMapper = userMapper;
        this.goodsStockMapper = goodsStockMapper;
        this.goodsService = goodsService;
        this.shopMapper = shopMapper;
    }

    public OrderResponse createOrder(OrderInfo orderInfo, Long userId) {
        Map<Long, Goods> idToGoodsMap = getIdToGoodsMap(orderInfo.getGoods());
        Order createOrder = createOrderViaRpc(orderInfo, userId, idToGoodsMap);
        return generateResponse(createOrder, idToGoodsMap, orderInfo.getGoods());
    }

    private OrderResponse generateResponse(Order createOrder, Map<Long, Goods> idToGoodsMap, List<GoodsInfo> goodsInfo) {
        OrderResponse response = new OrderResponse(createOrder);
        Long shopId = new ArrayList<>(idToGoodsMap.values()).get(0).getShopId();
        response.setShop(shopMapper.selectByPrimaryKey(shopId));
        response.setGoods(
                goodsInfo
                        .stream()
                        .map(goods -> toGoodsWithNumber(goods, idToGoodsMap))
                        .collect(toList())

        );
        return response;
    }

    //Transactional两个坑:1.相对应方法为public
    // 2.只能是别人调用deductstock这个方法,this调用不行!!
    @Transactional
    public void deductStock(OrderInfo orderInfo) {
        for (GoodsInfo goodsInfo : orderInfo.getGoods()) {
            if (goodsStockMapper.deductStock(goodsInfo) <= 0) {
                LOGGER.error("扣减库存失败, 商品id: " + goodsInfo.getId() + "，数量：" + goodsInfo.getNumber());
                throw HttpException.gone("扣减库存失败!");
            }
        }
    }

    private GoodsWithNumber toGoodsWithNumber(GoodsInfo goodsInfo, Map<Long, Goods> idToGoodsMap) {
        GoodsWithNumber ret = new GoodsWithNumber(idToGoodsMap.get(goodsInfo.getId()));
        ret.setNumber(goodsInfo.getNumber());
        return ret;

    }

    private Order createOrderViaRpc(OrderInfo orderInfo, Long userId, Map<Long, Goods> idToGoodsMap) {
        Order order = new Order();
        order.setUserId(userId);
        order.setShopId(new ArrayList<>(idToGoodsMap.values()).get(0).getShopId());
        order.setStatus(DataStatus.PENDING.getName());
        String address = orderInfo.getAddress() == null ?
                userMapper.selectByPrimaryKey(userId).getAddress() :
                orderInfo.getAddress();

        order.setAddress(address);        order.setTotalPrice(calculateTotalPrice(orderInfo, idToGoodsMap));

        return orderRpcService.createOrder(orderInfo, order);
    }

    private Long calculateTotalPrice(OrderInfo orderInfo, Map<Long, Goods> idToGoodsMap) {
        long result = 0;

        for (GoodsInfo goodsInfo : orderInfo.getGoods()) {
            Goods goods = idToGoodsMap.get(goodsInfo.getId());
            if (goods == null) {
                throw HttpException.badRequest("goods id非法：" + goodsInfo.getId());
            }
            if (goodsInfo.getNumber() <= 0) {
                throw HttpException.badRequest("number非法：" + goodsInfo.getNumber());
            }

            result = result + goods.getPrice() * goodsInfo.getNumber();
        }
        return result;
    }

    private Map<Long, Goods> getIdToGoodsMap(List<GoodsInfo> goodsInfo) {
        if (goodsInfo.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Long> goodsId = goodsInfo
                .stream()
                .map(GoodsInfo::getId)
                .collect(toList());
        return goodsService.getIdToGoodsMap(goodsId);
    }

    public OrderResponse deleteOrder(long orderId, long userId) {
        return toOrderResponse(orderRpcService.deleteOrder(orderId, userId));
    }

    public PageResponse<OrderResponse> getOrder(long userId, Integer pageNum, Integer pageSize, DataStatus status) {
        PageResponse<RpcOrderGoods> rpcOrderGoods = orderRpcService.getOrder(userId, pageNum, pageSize, status);

        List<GoodsInfo> goodIds = rpcOrderGoods
                .getData()
                .stream()
                .map(RpcOrderGoods::getGoods)
                .flatMap(List::stream)
                .collect(toList());

        Map<Long, Goods> idToGoodsMap = getIdToGoodsMap(goodIds);

        List<OrderResponse> orders = rpcOrderGoods.getData()
                .stream()
                .map(order -> generateResponse(order.getOrder(), idToGoodsMap, order.getGoods()))
                .collect(toList());


        return PageResponse.pagedData(
                rpcOrderGoods.getPageNum(),
                rpcOrderGoods.getPageSize(),
                rpcOrderGoods.getTotalPage(),
                orders
        );
    }


    public OrderResponse updateExpressInformation(Order order, long userId) {
        doGetOrderById(userId, order.getId());

        Order copy = new Order();
        copy.setId(order.getId());
        copy.setExpressId(order.getExpressId());
        copy.setExpressCompany(order.getExpressCompany());
        return toOrderResponse(orderRpcService.updateOrder(copy));
    }

    public RpcOrderGoods doGetOrderById(long userId, long orderId) {
        RpcOrderGoods orderInDatabase = orderRpcService.getOrderById(orderId);
        if (orderInDatabase == null) {
            throw HttpException.notFound("订单未找到: " + orderId);
        }

        Shop shop = shopMapper.selectByPrimaryKey(orderInDatabase.getOrder().getShopId());
        if (shop == null) {
            throw HttpException.notFound("店铺未找到: " + orderInDatabase.getOrder().getShopId());
        }

        if (shop.getOwnerUserId() != userId && orderInDatabase.getOrder().getUserId() != userId) {
            throw HttpException.forbidden("无权访问！");
        }
        return orderInDatabase;
    }

    public OrderResponse getOrderById(long userId, long orderId) {
        return toOrderResponse(doGetOrderById(userId, orderId));
    }

    public OrderResponse updateOrderStatus(Order order, long userId) {
        doGetOrderById(userId, order.getId());

        Order copy = new Order();
        copy.setId(order.getId());
        copy.setStatus(order.getStatus());
        return toOrderResponse(orderRpcService.updateOrder(copy));
    }

    private OrderResponse toOrderResponse(RpcOrderGoods rpcOrderGoods) {
        Map<Long, Goods> idToGoodsMap = getIdToGoodsMap(rpcOrderGoods.getGoods());
        return generateResponse(rpcOrderGoods.getOrder(), idToGoodsMap, rpcOrderGoods.getGoods());
    }


}

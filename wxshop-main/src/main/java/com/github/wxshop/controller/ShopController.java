package com.github.wxshop.controller;


import com.github.api.data.PageResponse;
import com.github.wxshop.entity.Response;
import com.github.wxshop.generate.Shop;
import com.github.wxshop.service.ShopService;
import com.github.wxshop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class ShopController {
    private ShopService shopService;


    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    /**
     * @api {get} /shop 获取当前用户名下的所有店铺
     * @apiName GetShop
     * @apiGroup 店铺
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiParam {Number} pageNum 页数，从1开始
     * @apiParam {Number} pageSize 每页显示的数量
     *
     * @apiSuccess {Number} pageNum 页数，从1开始
     * @apiSuccess {Number} pageSize 每页显示的数量
     * @apiSuccess {Number} totalPage 共有多少页
     * @apiSuccess {Shop} data 店铺列表
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "pageNum": 1,
     *       "pageSize": 10,
     *       "totalPage": 5,
     *       "data": [
     *          {
     *              "id": 12345,
     *              "name": "我的店铺",
     *              "description": "我的苹果专卖店",
     *              "imgUrl": "https://img.url",
     *              "ownerUserId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *          },
     *          {
     *              ...
     *          }
     *       ]
     *     }
     *
     * @apiError 401 Unauthorized 若用户未登录
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     *
     * @param pageNum
     * @param pageSize
     * @return 店铺列表
     */
    @GetMapping("/shop")
    public PageResponse<Shop> getShop(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize) {
        return shopService.getShopByUserId(UserContext.getCurrentUser().getId(), pageNum, pageSize);
    }

    /**
     * @api {get} /shop/:id 获取指定ID的店铺
     * @apiName GetShopById
     * @apiGroup 店铺
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 Created
     *     {
     *       "data": {
     *              "id": 12345,
     *              "name": "我的店铺",
     *              "description": "我的苹果专卖店",
     *              "imgUrl": "https://img.url",
     *              "ownerUserId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *       }
     *     }
     *
     * @apiError 401 Unauthorized 若用户未登录
     * @apiError 404 Not found 若店铺未找到
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     * 根据id获取店铺
     * @param shopId 店铺id
     * @return 店铺
     */
    @GetMapping("/shop/{id}")
    public Response<Shop> getShop(@PathVariable("id") long shopId) {
        return Response.of(shopService.getShopById(shopId));
    }

    /**
     * @api {post} /shop 创建店铺
     * @apiName CreateShop
     * @apiGroup 店铺
     *
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiParamExample {json} Request-Example:
     *          {
     *              "id": 12345,
     *              "name": "我的店铺",
     *              "description": "我的苹果专卖店",
     *              "imgUrl": "https://img.url",
     *          }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *     {
     *       "data": {
     *              "id": 12345,
     *              "name": "我的店铺",
     *              "description": "我的苹果专卖店",
     *              "imgUrl": "https://img.url",
     *              "ownerUserId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *       }
     *     }
     *
     * @apiError 400 Bad Request 若用户的请求中包含错误
     * @apiError 401 Unauthorized 若用户未登录
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     *
     * @param shop
     * @param response
     * @return 新创建的店铺
     */
    @PostMapping("/shop")
    public Response<Shop> createShop(@RequestBody Shop shop, HttpServletResponse response) {
        Response<Shop> ret = Response.of(shopService.createShop(shop, UserContext.getCurrentUser().getId()));
        response.setStatus(HttpStatus.CREATED.value());
        return ret;
    }

    /**
     * @api {PATCH} /shop/:id 修改店铺
     * @apiName UpdateShop
     * @apiGroup 店铺
     *
     * @apiParam {Number} id 店铺ID
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiParamExample {json} Request-Example:
     *          {
     *              "id": 12345,
     *              "name": "我的店铺",
     *              "description": "我的苹果专卖店",
     *              "imgUrl": "https://img.url",
     *          }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "data": {
     *              "id": 12345,
     *              "name": "我的店铺",
     *              "description": "我的苹果专卖店",
     *              "imgUrl": "https://img.url",
     *              "ownerUserId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *       }
     *     }
     *
     * @apiError 400 Bad Request 若用户的请求中包含错误
     * @apiError 404 Not Found 若店铺未找到
     * @apiError 401 Unauthorized 若用户未登录
     * @apiError 403 Forbidden 若用户尝试修改非自己管理店铺
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     * 更新店铺的信息
     * @param id
     * @param shop
     * @return 更新后的店铺
     */
    @PatchMapping("/shop/{id}")
    public Response<Shop> updateShop(@PathVariable("id") Long id,
                                     @RequestBody Shop shop) {
        shop.setId(id);
        return Response.of(shopService.updateShop(shop, UserContext.getCurrentUser().getId()));

    }


    /**
     * @api {DELETE} /shop/:id 删除店铺
     * @apiName DeleteShop
     * @apiGroup 店铺
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiParam {Number} id 店铺ID
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 204 No Content
     *     {
     *       "data": {
     *              "id": 12345,
     *              "name": "我的店铺",
     *              "description": "我的苹果专卖店",
     *              "imgUrl": "https://img.url",
     *              "ownerUserId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *       }
     *     }
     *
     * @apiError 400 Bad Request 若用户的请求中包含错误
     * @apiError 404 Not Found 若店铺未找到
     * @apiError 401 Unauthorized 若用户未登录
     * @apiError 403 Forbidden 若用户尝试删除非自己管理店铺
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     *
     * @param shopId 店铺id
     * @return 刚刚删除的店铺
     */
    @DeleteMapping("/shop/{id}")
    public Response<Shop> deleteShop(
            @PathVariable("id") Long shopId) {
        return Response.of(shopService.deleteShop(shopId, UserContext.getCurrentUser().getId()));

    }


}

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

    @GetMapping("/shop")
    public PageResponse<Shop> getShop(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize) {
        return shopService.getShopByUserId(UserContext.getCurrentUser().getId(), pageNum, pageSize);
    }


    @PostMapping("/shop")
    public Response<Shop> createShop(@RequestBody Shop shop, HttpServletResponse response) {
        Response<Shop> ret = Response.of(shopService.createShop(shop, UserContext.getCurrentUser().getId()));
        response.setStatus(HttpStatus.CREATED.value());
        return ret;
    }

    @PatchMapping("/shop/{id}")
    public Response<Shop> updateShop(@PathVariable("id") Long id,
                                     @RequestBody Shop shop,
                                     HttpServletResponse response) {
        shop.setId(id);
        return Response.of(shopService.updateShop(shop, UserContext.getCurrentUser().getId()));

    }


    @DeleteMapping("/shop/{id}")
    public Response<Shop> deleteShop(
            @PathVariable("id") Long shopId) {
        return Response.of(shopService.deleteShop(shopId, UserContext.getCurrentUser().getId()));

    }


}

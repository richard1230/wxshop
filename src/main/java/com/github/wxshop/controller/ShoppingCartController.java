package com.github.wxshop.controller;

import com.github.wxshop.entity.PageResponse;
import com.github.wxshop.entity.ShoppingCartData;
import com.github.wxshop.service.ShoppingCartService;
import com.github.wxshop.service.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ShoppingCartController {
    private static Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }



    @GetMapping("/shoppingCart")
    public PageResponse<ShoppingCartData> getShoppingCart(
            @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize
    ){
        return shoppingCartService.getShoppingCartOfUser(UserContext.getCurrentUser().getId(),
                pageNum,
                pageSize
                );
    }


    @PostMapping("/shoppingCart")
    public void addToShoppingCart(@RequestBody AddToShoppingCartRequest request) {
    }


    /**
     * 这个类接受
     * 你post过来的数据
     * */
    public static class AddToShoppingCartRequest {
        List<AddToShoppingCartItem> goods;

        public List<AddToShoppingCartItem> getGoods() {
            return goods;
        }

        public void setGoods(List<AddToShoppingCartItem> goods) {
            this.goods = goods;
        }
    }

    //shoppingart接口接受到的json数据
    public static class AddToShoppingCartItem {
        //long类型的
        long id;
        int number;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }


    public void deleteShoppingCart() {
    }


}

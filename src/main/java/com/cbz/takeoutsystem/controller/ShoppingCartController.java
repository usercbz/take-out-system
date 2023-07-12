package com.cbz.takeoutsystem.controller;

import com.cbz.takeoutsystem.entity.ShoppingCart;
import com.cbz.takeoutsystem.service.impl.ShoppingCartServiceImpl;
import com.cbz.takeoutsystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @GetMapping("list")
    public Result<List<ShoppingCart>> getCartList() {
        return shoppingCartService.queryCartList();
    }

    /**
     * 购物车中添加商品
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("add")
    public Result<ShoppingCart> addDishToCart(@RequestBody ShoppingCart shoppingCart) {
//        log.info(shoppingCart.toString());
//        return null;
        return shoppingCartService.addDishToCart(shoppingCart);
    }

    @PostMapping("edit")
    public Result<Object> updateCart(@RequestBody ShoppingCart shoppingCart) {
//        log.info(shoppingCart.toString());
        return shoppingCartService.subDishFormCart(shoppingCart);
    }

    @DeleteMapping("clean")
    public Result<Object> clearShoppingCart() {
        return shoppingCartService.clearShoppingCart();
    }

}

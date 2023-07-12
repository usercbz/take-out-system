package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.entity.ShoppingCart;
import com.cbz.takeoutsystem.utils.Result;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    Result<List<ShoppingCart>> queryCartList();

    Result<ShoppingCart> addDishToCart(ShoppingCart shoppingCart);

    Result<Object> subDishFormCart(ShoppingCart shoppingCart);

    Result<Object> clearShoppingCart();

}

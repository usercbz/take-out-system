package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.entity.Dish;
import com.cbz.takeoutsystem.entity.Setmeal;
import com.cbz.takeoutsystem.entity.ShoppingCart;
import com.cbz.takeoutsystem.mapper.ShoppingCartMapper;
import com.cbz.takeoutsystem.service.ShoppingCartService;
import com.cbz.takeoutsystem.utils.Result;
import com.cbz.takeoutsystem.utils.UserHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    private final DishServiceImpl dishService;
    private final SetmealServiceImpl setmealService;

    public ShoppingCartServiceImpl(DishServiceImpl dishService, SetmealServiceImpl setmealService) {
        this.dishService = dishService;
        this.setmealService = setmealService;
    }

    /**
     * 查询购物车列表
     *
     * @return 购物车列表
     */
    @Override
    public Result<List<ShoppingCart>> queryCartList() {
        List<ShoppingCart> list = list();
        return Result.success(list);
    }

    /**
     * 添加菜品到购物车
     *
     * @param shoppingCart 购物车清单
     * @return 购物清单数据
     */
    @Override
    public Result<ShoppingCart> addDishToCart(ShoppingCart shoppingCart) {

        ShoppingCart shoppingCart1 = editShoppingCart(shoppingCart, true);
        if (shoppingCart1 == null) {
            return Result.error("添加失败！");
        }
        return Result.success(shoppingCart1);
    }

    @Override
    public Result<Object> subDishFormCart(ShoppingCart shoppingCart) {
        return Result.success(editShoppingCart(shoppingCart, false));
    }

    /**
     * 清空购物车
     *
     * @return 处理结果
     */
    @Override
    public Result<Object> clearShoppingCart() {
        return Result.success(remove(new QueryWrapper<>()));
    }

    /**
     * 购物车操作
     *
     * @param shoppingCart 操作数据
     * @param isAdd        是否添加 TRUE -- ‘+’ ，FALSE -- ‘-’
     * @return 操作是否成功
     */
    private ShoppingCart editShoppingCart(ShoppingCart shoppingCart, boolean isAdd) {

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        Dish dish = dishService.getById(dishId);
        Setmeal setmeal = setmealService.getById(setmealId);
        //判断
        if (dish == null && setmeal == null) {
            return null;
        }

        //判断是否是套餐
        boolean isSetmeal = setmeal != null;
        //查询数据库中的购物清单
        LambdaQueryWrapper<ShoppingCart> queryWrapper = Wrappers.lambdaQuery(ShoppingCart.class)
                .eq((isSetmeal ? ShoppingCart::getSetmealId : ShoppingCart::getDishId), (isSetmeal ? setmealId : dishId));
        //购物清单信息
        ShoppingCart shopData = getOne(queryWrapper);

        Integer number;

        if (isAdd) {
            //判断
            if (shopData == null) {
                //不存在、添加
                shoppingCart.setUserId(UserHolder.getUser().getId());
                if (save(shoppingCart)) {
                    //成功 、返回原有对象
                    return getById(shoppingCart.getId());
                }
                return null;
            }
            //存在、number ++
            number = shopData.getNumber();
            shopData.setNumber(++number);

        } else {
            //判断
            if (shopData == null) {
                return null;
            }
            number = shopData.getNumber();
            //判断number
            if (number == 1) {
                //number  = 0  删除清单
                removeById(shopData.getId());
                return new ShoppingCart();
            }
            //大于0 、number --
            shopData.setNumber(--number);
        }
        //插入、更新
        if (updateById(shopData)) {
            //成功
            return shopData;
        }
        //失败
        return null;
    }
}

package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.entity.Dish;
import com.cbz.takeoutsystem.dto.DishDto;
import com.cbz.takeoutsystem.utils.Result;

import java.util.List;

public interface DishService extends IService<Dish> {
    Result<Object> queryDishesPage(Integer page, Integer pageSize, String name);

    Result<DishDto> queryDishById(Integer id);

    Result<Object> addDish(DishDto dishDto);

    Result<Object> editDish(DishDto dishDto);

    Result<Object> deleteDishByIds(String ids);

    Result<Object> changeDishStatusByIds(String ids, Integer status);

    Result<List<DishDto>> queryDishList(Integer categoryId, String name, Integer status);
}

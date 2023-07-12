package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.dto.DishDto;
import com.cbz.takeoutsystem.dto.SetmealDto;
import com.cbz.takeoutsystem.entity.Setmeal;
import com.cbz.takeoutsystem.utils.Result;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    Result<Object> querySetmealPage(Integer page, Integer pageSize, String name);

    Result<SetmealDto> querySetmealById(Integer id);

    Result<Object> updateSetmealStatusByIds(String ids, Integer status);

    Result<Object> deleteSetmealByIds(String ids);

    Result<Object> addSetmeal(SetmealDto setmealDto);

    Result<Object> editSetmeal(SetmealDto setmealDto);

    Result<List<SetmealDto>> querySetmealList(Integer categoryId, Integer status);

    Result<List<DishDto>> getSetmealDishes(Long id);
}

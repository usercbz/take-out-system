package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.dto.DishDto;
import com.cbz.takeoutsystem.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    List<SetmealDish> querySetmealDishBySetmealId(Long id);

    List<DishDto> queryDishDtoBySetmealId(Long id);
}

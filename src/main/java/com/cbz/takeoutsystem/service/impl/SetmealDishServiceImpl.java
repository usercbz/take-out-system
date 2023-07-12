package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.dto.DishDto;
import com.cbz.takeoutsystem.entity.Dish;
import com.cbz.takeoutsystem.entity.SetmealDish;
import com.cbz.takeoutsystem.mapper.SetmealDishMapper;
import com.cbz.takeoutsystem.service.SetmealDishService;
import com.cbz.takeoutsystem.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

    @Autowired
    private DishServiceImpl dishService;

    @Override
    public List<SetmealDish> querySetmealDishBySetmealId(Long id) {
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        return list(wrapper);
    }

    /**
     * 获取套餐菜品信息
     *
     * @param id 套餐id
     * @return 菜品信息列表
     */
    @Override
    public List<DishDto> queryDishDtoBySetmealId(Long id) {

        //根据套餐id 查询套餐菜品信息表
        List<SetmealDish> setmealDishes = querySetmealDishBySetmealId(id);

        List<DishDto> dishDtos = new ArrayList<>();

        //循环添加菜品信息
        for (SetmealDish setmealDish : setmealDishes) {
            //根据菜品id查询菜品
            Dish dish = dishService.getById(setmealDish.getDishId());
            //封装dto
            DishDto dishDto = BeanUtils.toDTO(DishDto.class, dish);
            //设置份数
            dishDto.setCopies(setmealDish.getCopies());
            //封装成list
            dishDtos.add(dishDto);
        }
        return dishDtos;
    }
}

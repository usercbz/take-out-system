package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.entity.DishFlavor;
import com.cbz.takeoutsystem.mapper.DishFlavorMapper;
import com.cbz.takeoutsystem.service.DishFlavorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    /**
     * 添加口味
     *
     * @param flavors 菜品口味列表
     * @return 处理结果
     */
    @Override
    public boolean addFlavors(List<DishFlavor> flavors) {
        //添加
        return saveBatch(flavors);
    }

    /**
     * 查询菜品口味
     *
     * @param id 菜品id
     * @return 口味列表
     */
    @Override
    public List<DishFlavor> queryFlavorsByDishId(Long id) {
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);
        return list(wrapper);
    }
}

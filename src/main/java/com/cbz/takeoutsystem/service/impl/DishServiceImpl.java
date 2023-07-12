package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.entity.Dish;
import com.cbz.takeoutsystem.dto.DishDto;
import com.cbz.takeoutsystem.entity.DishFlavor;
import com.cbz.takeoutsystem.mapper.DishMapper;
import com.cbz.takeoutsystem.service.DishService;
import com.cbz.takeoutsystem.utils.BeanUtils;
import com.cbz.takeoutsystem.utils.Result;
import com.cbz.takeoutsystem.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorServiceImpl dishFlavorService;

    /**
     * 获取菜品信息
     *
     * @param page     页起始下标
     * @param pageSize 页大小
     * @param name     菜品名字
     * @return 处理结果集
     */
    @Override
    public Result<Object> queryDishesPage(Integer page, Integer pageSize, String name) {
        LambdaQueryWrapper<Dish> wrapper = null;
        if (name != null) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.like(Dish::getName, name);
        }
        return Result.success(page(new Page<>(page, pageSize), wrapper));
    }

    /**
     * 根据id查询菜品
     *
     * @param id 菜品id
     * @return 菜品数据
     */
    @Override
    public Result<DishDto> queryDishById(Integer id) {
        Dish dish = getById(id);

        if (dish == null) {
            return Result.error("查询失败！");
        }
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);

        DishDto dishDto = BeanUtils.toDTO(DishDto.class, dish);
        dishDto.setFlavors(flavors);

        //返回
        return Result.success(dishDto);
    }

    /**
     * 添加菜品
     *
     * @param dishDto 数据集 --菜品信息
     * @return 处理结果集
     */
    @Transactional
    @Override
    public Result<Object> addDish(DishDto dishDto) {
        //生成code
        String code = StrUtils.createUUID(true);
        //设置编码
        dishDto.setCode(code);
        //添加、保存
        if (save(dishDto)) {
            //成功
            List<DishFlavor> flavors = dishDto.getFlavors();

            if (flavors != null && flavors.size() != 0) {
                for (DishFlavor flavor : flavors) {
                    flavor.setDishId(dishDto.getId());
                }
                //添加口味
                dishFlavorService.addFlavors(flavors);
            }
            //成功回调
            return Result.success(null);
        }
        //失败回调
        return Result.error("添加失败！");
    }

    /**
     * 修改菜品
     *
     * @param dishDto 数据集
     * @return 处理结果
     */
    @Transactional
    @Override
    public Result<Object> editDish(DishDto dishDto) {
        //修改菜品
        updateById(dishDto);
        //修改口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null) {
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dishDto.getId());
            //删除口味
            dishFlavorService.remove(wrapper);
            //添加口味
            if (flavors.size() != 0) {
                dishFlavorService.addFlavors(flavors);
            }
        }
        //成功
        return Result.success(null);
    }

    /**
     * 删除菜品
     *
     * @param ids id序列
     * @return 处理结果集
     */
    @Override
    public Result<Object> deleteDishByIds(String ids) {
        if (ids == null) {
            return Result.error("删除失败！");
        }
        //分割id
        List<Integer> idList = StrUtils.splitIds(ids);
        //删除
        if (removeBatchByIds(idList)) {
            //成功回调
            return Result.success(null);
        }
        //失败回调
        return Result.error("删除失败！");
    }

    /**
     * 修改商品状态
     *
     * @param ids    商品id序列
     * @param status 目标状态值
     * @return 处理结果集
     */
    @Override
    public Result<Object> changeDishStatusByIds(String ids, Integer status) {
        if (ids == null || status == null) {
            return Result.error("修改失败");
        }
        //分割id
        List<Integer> idList = StrUtils.splitIds(ids);
        //根据ids查询
        List<Dish> dishes = listByIds(idList);
        if (dishes != null && dishes.size() != 0) {
            //修改 status的值
            for (Dish dish : dishes) {
                dish.setStatus(status);
                dish.setUpdateTime(null);
                dish.setUpdateUser(null);
            }
            //修改
            if (updateBatchById(dishes)) {
                //成功回调
                return Result.success(null);
            }
        }
        //失败回调
        return Result.error("修改失败");
    }

    /**
     * 查找符合要求的菜品
     *
     * @param categoryId 分类id
     * @param name       菜品名
     * @param status     状态
     * @return 符合条件的菜品列表
     */
    @Override
    public Result<List<DishDto>> queryDishList(Integer categoryId, String name, Integer status) {
        //查询符合条件的商品
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(Dish::getCategoryId, categoryId);
        }
        if (name != null) {
            wrapper.like(Dish::getName, name);
        }
        if (status != null) {
            wrapper.eq(Dish::getStatus, status);
        }
        List<Dish> dishes = list(wrapper);

        return Result.success(toDishDtoList(dishes));
    }

    /**
     * 封装菜品和口味数据
     *
     * @param dishes 菜品列表
     * @return 菜品口味列表
     */
    public List<DishDto> toDishDtoList(List<Dish> dishes) {

        List<DishDto> dishDtos = new ArrayList<>();

        for (Dish dish : dishes) {
            List<DishFlavor> dishFlavors = dishFlavorService.queryFlavorsByDishId(dish.getId());
            DishDto dishDto = BeanUtils.toDTO(DishDto.class, dish);
            dishDto.setFlavors(dishFlavors);
            dishDtos.add(dishDto);
        }
        return dishDtos;
    }

}

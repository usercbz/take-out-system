package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.dto.DishDto;
import com.cbz.takeoutsystem.dto.SetmealDto;
import com.cbz.takeoutsystem.entity.Dish;
import com.cbz.takeoutsystem.entity.Setmeal;
import com.cbz.takeoutsystem.entity.SetmealDish;
import com.cbz.takeoutsystem.mapper.SetmealMapper;
import com.cbz.takeoutsystem.service.SetmealService;
import com.cbz.takeoutsystem.utils.BeanUtils;
import com.cbz.takeoutsystem.utils.Result;
import com.cbz.takeoutsystem.utils.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private SetmealDishServiceImpl setmealDishService;

    /**
     * 查询分页数据
     *
     * @param page     页起始索引
     * @param pageSize 页大小
     * @param name     套餐名
     * @return 处理结果集
     */
    @Override
    public Result<Object> querySetmealPage(Integer page, Integer pageSize, String name) {
        //条件
        LambdaQueryWrapper<Setmeal> wrapper = null;
        if (name != null) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Setmeal::getName, name);
        }
        //查询
        Page<Setmeal> setmealPage = page(new Page<>(page, pageSize), wrapper);

        HashMap<String, Object> map = new HashMap<>();

        //封装setmeal
        List<Setmeal> setmeals = setmealPage.getRecords();

        if (setmeals.size() == 0) {
            return Result.success(setmealPage);
        }

        Long categoryId = setmeals.get(0).getCategoryId();
        String categoryName = categoryService.getById(categoryId).getName();
        List<SetmealDto> setmealDtos = new ArrayList<>();

        for (Setmeal setmeal : setmeals) {

            SetmealDto setmealDto = BeanUtils.toDTO(SetmealDto.class, setmeal);
            setmealDto.setCategoryName(categoryName);
            setmealDtos.add(setmealDto);
        }
        //封装结果集
        map.put("records", setmealDtos);
        map.put("total", setmealPage.getTotal());
        //返回
        return Result.success(map);
    }

    @Override
    public Result<SetmealDto> querySetmealById(Integer id) {
        //根据id查询
        if (id == null) {
            return Result.error("查询失败");
        }
        Setmeal setmeal = getById(id);

        if (setmeal == null) {
            return Result.error("未查询到数据！");
        }

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(wrapper);

        SetmealDto setmealDto = BeanUtils.toDTO(SetmealDto.class, setmeal);
        setmealDto.setSetmealDishes(setmealDishes);

        return Result.success(setmealDto);
    }

    /**
     * 修改转态值
     *
     * @param ids    id序列
     * @param status 目标状态值
     * @return 处理结果集
     */
    @Override
    public Result<Object> updateSetmealStatusByIds(String ids, Integer status) {
        //分割id序列
        List<Integer> idList = StrUtils.splitIds(ids);
        //查询相应数据
        List<Setmeal> setmeals = listByIds(idList);

        if (setmeals != null && setmeals.size() != 0) {
            for (Setmeal setmeal : setmeals) {
                //设置状态值
                setmeal.setStatus(status);
                //置空
                setmeal.setUpdateTime(null);
                setmeal.setUpdateUser(null);
            }
            //修改状态
            if (updateBatchById(setmeals)) {
                //成功
                return Result.success(null);
            }
        }
        //失败
        return Result.error("修改失败！");
    }

    /**
     * 删除套餐
     *
     * @param ids id序列
     * @return 处理结果集
     */
    @Override
    public Result<Object> deleteSetmealByIds(String ids) {
        //分割id序列
        List<Integer> idList = StrUtils.splitIds(ids);
        //根据id删除
        if (removeBatchByIds(idList)) {
            //删除成功
            //成功回调
            return Result.success(null);
        }
        //失败回调
        return Result.error("删除失败！");
    }

    /**
     * 添加套餐
     *
     * @param setmealDto 源数据
     * @return 处理结果集
     */
    @Transactional
    @Override
    public Result<Object> addSetmeal(SetmealDto setmealDto) {
        //判断
        if (setmealDto == null) {
            return Result.error("添加失败！");
        }
        //生成code
        String code = StrUtils.createUUID(true);
        //设置code
        setmealDto.setCode(code);
        //添加、保存
        if (save(setmealDto)) {
            //成功
            List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
            if (setmealDishes != null && setmealDishes.size() != 0) {
                //获取套餐id
                Long setmealId = setmealDto.getId();
                for (SetmealDish setmealDish : setmealDishes) {
                    //设置id
                    setmealDish.setSetmealId(setmealId);
                }
                //添加
                setmealDishService.saveBatch(setmealDishes);
            }
            return Result.success(null);
        }

        //失败
        return Result.error("添加失败！");
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDto 修改后信息
     * @return 处理结果集
     */
    @Override
    public Result<Object> editSetmeal(SetmealDto setmealDto) {

        //修改
        updateById(setmealDto);

        //修改套餐中菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        if (setmealDishes != null) {
            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SetmealDish::getId, setmealDto.getId());
            //删除原有
            setmealDishService.remove(wrapper);

            //添加套餐菜品
            if (setmealDishes.size() != 0) {
                for (SetmealDish setmealDish : setmealDishes) {
                    setmealDish.setSetmealId(setmealDto.getId());
                }
                setmealDishService.saveBatch(setmealDishes);
            }
        }
        //返回
        return Result.success(null);
    }

    /**
     * 查询套餐列表
     *
     * @param categoryId 分类id
     * @param status     状态
     * @return 套餐信息列表
     */
    @Override
    public Result<List<SetmealDto>> querySetmealList(Integer categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(Setmeal::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Setmeal::getStatus, status);
        }
        List<Setmeal> setmeals = list(wrapper);

        List<SetmealDto> setmealDtos = new ArrayList<>();

        for (Setmeal setmeal : setmeals) {
            SetmealDto setmealDto = BeanUtils.toDTO(SetmealDto.class, setmeal);
            setmealDto.setSetmealDishes(setmealDishService.querySetmealDishBySetmealId(setmeal.getId()));
            setmealDtos.add(setmealDto);
        }

        return Result.success(setmealDtos);
    }

    /**
     * 查询套餐内的菜品
     *
     * @param id 套餐id
     * @return 套餐菜品信息列表
     */
    @Override
    public Result<List<DishDto>> getSetmealDishes(Long id) {

        List<DishDto> dishDtos = setmealDishService.queryDishDtoBySetmealId(id);
        return Result.success(dishDtos);
    }
}

package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.entity.Category;
import com.cbz.takeoutsystem.mapper.CategoryMapper;
import com.cbz.takeoutsystem.service.CategoryService;
import com.cbz.takeoutsystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    /**
     * 查询数据页
     *
     * @param page     页起始索引
     * @param pageSize 页大小
     * @return 处理结果集
     */
    @Override
    public Result<Object> queryCategoriesPage(Integer page, Integer pageSize) {
        //返回数据页
        return Result.success(page(new Page<>(page, pageSize)));
    }

    /**
     * 根据id查询分类
     *
     * @param id id
     * @return 处理结果集
     */
    @Override
    public Result<Category> queryCategoryById(Integer id) {
        Category category = getById(id);
        if (category == null) {
            return Result.error("查询失败！");
        }
        return Result.success(category);
    }

    /**
     * 添加分类
     *
     * @param category 分类数据信息
     * @return 处理结果集
     */
    @Override
    public Result<Object> addCategory(Category category) {
//        log.info(category.toString());

        if (save(category)) {
            return Result.success(null);
        }
        return Result.error("添加失败！");
    }

    /**
     * 修改分类信息
     *
     * @param category 分类数据信息
     * @return 处理结果集
     */
    @Override
    public Result<Object> editCategory(Category category) {
        if (updateById(category)) {
            return Result.success(null);
        }

        return Result.error("修改失败！");
    }

    /**
     * 根据id删除分类信息
     *
     * @param id id
     * @return 处理结果集
     */
    @Override
    public Result<Object> deleteCategory(String id) {
        if (removeById(Long.valueOf(id))) {
            return Result.success(null);
        }
        return Result.error("删除失败！");
    }

    /**
     * 查询分类列表
     *
     * @param type 类型
     * @return 处理结果集
     */
    @Override
    public Result<List<Category>> getCategoryList(Integer type) {
        LambdaQueryWrapper<Category> wrapper = null;
        if (type != null) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Category::getType, type);
        }
        List<Category> list = list(wrapper);
        return Result.success(list);
    }
}

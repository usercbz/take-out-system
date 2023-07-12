package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.entity.Category;
import com.cbz.takeoutsystem.utils.Result;

import java.util.List;

public interface CategoryService extends IService<Category> {

    Result<Object> queryCategoriesPage(Integer page, Integer pageSize);

    Result<Category> queryCategoryById(Integer id);

    Result<Object> addCategory(Category category);

    Result<Object> editCategory(Category category);

    Result<Object> deleteCategory(String id);

    Result<List<Category>> getCategoryList(Integer type);

}

package com.cbz.takeoutsystem.controller;

import com.cbz.takeoutsystem.entity.Category;
import com.cbz.takeoutsystem.service.impl.CategoryServiceImpl;
import com.cbz.takeoutsystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("page")
    public Result<Object> getCategoriesPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return categoryService.queryCategoriesPage(page, pageSize);
    }

    @GetMapping("/{id}")
    public Result<Category> getCategoryById(@PathVariable Integer id) {
        return categoryService.queryCategoryById(id);
    }

    @PostMapping
    public Result<Object> addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @PutMapping
    public Result<Object> editCategory(@RequestBody Category category){
        return categoryService.editCategory(category);
    }

    @DeleteMapping
    public Result<Object> deleteCategory(@RequestParam("ids") String id){
        return categoryService.deleteCategory(id);
    }

    @GetMapping("list")
    public Result<List<Category>> getCategoryList(@RequestParam(required = false) Integer type){
        return categoryService.getCategoryList(type);
    }
}

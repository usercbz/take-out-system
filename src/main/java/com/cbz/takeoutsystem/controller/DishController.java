package com.cbz.takeoutsystem.controller;

import com.cbz.takeoutsystem.dto.DishDto;
import com.cbz.takeoutsystem.entity.Dish;
import com.cbz.takeoutsystem.service.impl.DishServiceImpl;
import com.cbz.takeoutsystem.utils.BeanUtils;
import com.cbz.takeoutsystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("dish")
public class DishController {
    @Autowired
    private DishServiceImpl dishService;

    @GetMapping("page")
    public Result<Object> getDishesPage(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String name) {
        return dishService.queryDishesPage(page, pageSize, name);
    }

    @GetMapping("/{id}")
    public Result<DishDto> getDishById(@PathVariable Integer id) {
        return dishService.queryDishById(id);
    }

    @PostMapping
    public Result<Object> addDish(@RequestBody DishDto dishDto) {
//        log.info(dishDto.toString());
        return dishService.addDish(dishDto);
    }

    @PutMapping
    public Result<Object> editDish(@RequestBody DishDto dishDto) {
//        log.info(BeanUtils.toBean(Dish.class, dishDto).toString());
//        log.info(dishDto.getFlavors().toString());
//        return null;
        return dishService.editDish(dishDto);
    }

    @DeleteMapping
    public Result<Object> deleteDish(@RequestParam String ids) {
//        log.info(ids);
        return dishService.deleteDishByIds(ids);
    }

    @PostMapping("status/{status}")
    public Result<Object> editDishStatusIds(@RequestParam String ids, @PathVariable Integer status) {
//        log.info(ids,status);
        return dishService.changeDishStatusByIds(ids, status);
    }

    @GetMapping("list")
    public Result<List<DishDto>> getDishList(@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String name, @RequestParam(required = false) Integer status) {
//        log.info(categoryId.toString());
        return dishService.queryDishList(categoryId, name,status);
    }

}

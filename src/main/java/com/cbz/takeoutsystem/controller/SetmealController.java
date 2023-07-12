package com.cbz.takeoutsystem.controller;

import com.cbz.takeoutsystem.dto.DishDto;
import com.cbz.takeoutsystem.dto.SetmealDto;
import com.cbz.takeoutsystem.entity.Dish;
import com.cbz.takeoutsystem.entity.Setmeal;
import com.cbz.takeoutsystem.service.impl.SetmealServiceImpl;
import com.cbz.takeoutsystem.utils.BeanUtils;
import com.cbz.takeoutsystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Autowired
    private SetmealServiceImpl setmealService;

    @GetMapping("page")
    public Result<Object> getSetmealPage(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String name) {
        return setmealService.querySetmealPage(page, pageSize, name);
    }

    @GetMapping("/{id}")
    public Result<SetmealDto> getSetmealById(@PathVariable Integer id) {
        return setmealService.querySetmealById(id);
    }

    @PostMapping
    public Result<Object> addSetmeal(@RequestBody SetmealDto setmealDto) {
        return setmealService.addSetmeal(setmealDto);
    }


    @PutMapping
    public Result<Object> editSetmeal(@RequestBody SetmealDto setmealDto) {

        return setmealService.editSetmeal(setmealDto);
    }

    @DeleteMapping
    public Result<Object> deleteSetmealByIds(@RequestParam String ids) {
        return setmealService.deleteSetmealByIds(ids);
    }

    @PostMapping("/status/{status}")
    public Result<Object> changeSetmealStatusByIds(@RequestParam String ids, @PathVariable Integer status) {
        return setmealService.updateSetmealStatusByIds(ids, status);
    }

    @GetMapping("list")
    public Result<List<SetmealDto>> getSetmealList(@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) Integer status) {
        return setmealService.querySetmealList(categoryId, status);
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishDto>> getSetmealDishes(@PathVariable Long id) {
        return setmealService.getSetmealDishes(id);
    }

}

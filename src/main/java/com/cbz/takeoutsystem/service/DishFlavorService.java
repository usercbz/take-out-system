package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.entity.DishFlavor;

import java.util.List;

public interface DishFlavorService extends IService<DishFlavor> {
    boolean addFlavors(List<DishFlavor> flavors);

    List<DishFlavor> queryFlavorsByDishId(Long id);

}

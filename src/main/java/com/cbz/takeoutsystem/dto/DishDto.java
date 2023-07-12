package com.cbz.takeoutsystem.dto;

import com.cbz.takeoutsystem.entity.Dish;
import com.cbz.takeoutsystem.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}

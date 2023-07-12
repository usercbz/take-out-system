package com.cbz.takeoutsystem.dto;


import com.cbz.takeoutsystem.entity.Setmeal;
import com.cbz.takeoutsystem.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

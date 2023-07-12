package com.cbz.takeoutsystem;

import com.cbz.takeoutsystem.dto.DishDto;
import com.cbz.takeoutsystem.entity.OrderDetail;
import com.cbz.takeoutsystem.entity.Orders;
import com.cbz.takeoutsystem.entity.ShoppingCart;
import com.cbz.takeoutsystem.service.ShoppingCartService;
import com.cbz.takeoutsystem.service.impl.AddressBookServiceImpl;
import com.cbz.takeoutsystem.service.impl.DishServiceImpl;
import com.cbz.takeoutsystem.service.impl.ShoppingCartServiceImpl;
import com.cbz.takeoutsystem.utils.BeanUtils;
import com.cbz.takeoutsystem.utils.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

@SpringBootTest
class TakeOutSystemApplicationTests {

    @Autowired
    private DishServiceImpl dishService;

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private AddressBookServiceImpl addressBookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        Result<DishDto> result = dishService.queryDishById(1);
        try {
            System.out.println(objectMapper.writeValueAsString(result.getData()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    @Test
    void test01() {
        Class<DishDto> dishDtoClass = DishDto.class;

        Class<? super DishDto> superclass = dishDtoClass.getSuperclass();

        for (Field field : superclass.getDeclaredFields()) {
            System.out.println(field.getName());
        }
    }

    @Test
    void test03() {
        System.out.println(dishService.getById(null));
    }

    @Test
    void testCopyProperty() {
//        ShoppingCart shoppingCart = shoppingCartService.getById(15);
//        OrderDetail orderDetail = BeanUtils.copyProperty(new OrderDetail(), shoppingCart);
//        System.out.println(orderDetail);

        Orders orders = BeanUtils.copyProperty(new Orders(), addressBookService.getById(2));
        System.out.println(orders);
    }

}

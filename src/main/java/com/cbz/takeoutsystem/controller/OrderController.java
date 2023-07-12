package com.cbz.takeoutsystem.controller;

import com.cbz.takeoutsystem.entity.Orders;
import com.cbz.takeoutsystem.service.impl.OrderServiceImpl;
import com.cbz.takeoutsystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("page")
    public Result<Object> getOrdersPage(@RequestParam Integer page, @RequestParam Integer pageSize,
                                        @RequestParam(required = false) Long number, @RequestParam(required = false) String beginTime,
                                        @RequestParam(required = false) String endTime) {

        return orderService.queryOrdersPage(page, pageSize, number, beginTime, endTime);
    }

    @GetMapping("userPage")
    public Result<Object> getUserOrderPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return orderService.queryUserOrderPage(page, pageSize);
    }

    @PostMapping("submit")
    public Result<Object> addOrder(@RequestBody Orders order) {
//        log.info(order.toString());
        return orderService.addOrder(order);
    }

    @PostMapping("again")
    public Result<Object> orderAgain(@RequestBody Orders order) {
        return orderService.orderAgain(order);
    }

    @PutMapping
    public Result<Object> editOrderDetail(@RequestBody Orders order) {
        return orderService.editOrderStatus(order);
    }
}

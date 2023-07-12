package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.entity.Orders;
import com.cbz.takeoutsystem.utils.Result;

public interface OrderService extends IService<Orders> {
    Result<Object> queryOrdersPage(Integer page, Integer pageSize, Long number, String beginTime, String endTime);

    Result<Object> queryUserOrderPage(Integer page, Integer pageSize);

    Result<Object> addOrder(Orders order);

    Result<Object> orderAgain(Orders order);

    Result<Object> editOrderStatus(Orders order);
}

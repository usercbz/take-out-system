package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    List<OrderDetail> queryByOrderId(Long id);
}

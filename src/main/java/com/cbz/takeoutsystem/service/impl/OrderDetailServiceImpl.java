package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.entity.OrderDetail;
import com.cbz.takeoutsystem.mapper.OrderDetailMapper;
import com.cbz.takeoutsystem.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    /**
     * 根据id查询订单
     *
     * @param orderId 订单id
     * @return 目标订单信息
     */
    @Override
    public List<OrderDetail> queryByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, orderId);
        return list(wrapper);
    }
}

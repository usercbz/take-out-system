package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.dto.OrdersDto;
import com.cbz.takeoutsystem.entity.AddressBook;
import com.cbz.takeoutsystem.entity.OrderDetail;
import com.cbz.takeoutsystem.entity.Orders;
import com.cbz.takeoutsystem.entity.ShoppingCart;
import com.cbz.takeoutsystem.mapper.OrderMapper;
import com.cbz.takeoutsystem.service.OrderService;
import com.cbz.takeoutsystem.utils.BeanUtils;
import com.cbz.takeoutsystem.utils.Result;
import com.cbz.takeoutsystem.utils.StrUtils;
import com.cbz.takeoutsystem.utils.UserHolder;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    private final OrderDetailServiceImpl orderDetailService;

    private final ShoppingCartServiceImpl shoppingCartService;

    private final AddressBookServiceImpl addressBookService;

    public OrderServiceImpl(OrderDetailServiceImpl orderDetailService, ShoppingCartServiceImpl shoppingCartService, AddressBookServiceImpl addressBookService) {
        this.orderDetailService = orderDetailService;
        this.shoppingCartService = shoppingCartService;
        this.addressBookService = addressBookService;
    }

    /**
     * 查询订单页
     *
     * @param page      页起始索引
     * @param pageSize  页大小
     * @param number    订单号
     * @param beginTime 起始时间
     * @param endTime   结束时间
     * @return 处理结果集
     */
    @Override
    public Result<Object> queryOrdersPage(Integer page, Integer pageSize, Long number, String beginTime, String endTime) {
        LambdaQueryWrapper<Orders> wrapper = null;
        //判断条件
        if (number != null) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Orders::getNumber, number);
        }
        if (beginTime != null && endTime != null) {
            if (wrapper == null)
                wrapper = new LambdaQueryWrapper<>();
            wrapper.between(Orders::getOrderTime, beginTime, endTime);
        }
        //查询
        Page<Orders> ordersPage = page(new Page<>(page, pageSize), wrapper);
        //返回
        return Result.success(toOrderDtosMap(ordersPage));
    }

    /**
     * 查询用户订单数据
     *
     * @param page     页起始索引
     * @param pageSize 页大小
     * @return 订单详细信息
     */
    @Override
    public Result<Object> queryUserOrderPage(Integer page, Integer pageSize) {
        //查询
        Page<Orders> ordersPage = page(new Page<>(page, pageSize), Wrappers.lambdaQuery(Orders.class).orderByDesc(Orders::getOrderTime));
        HashMap<String, Object> map = toOrderDtosMap(ordersPage);
        map.put("pages", ordersPage.getTotal());
        //返回
        return Result.success(map);
    }

    /**
     * 添加订单
     *
     * @param order 订单详细信息
     * @return 处理结果
     */
    @Transactional
    @Override
    public Result<Object> addOrder(Orders order) {
        //1.获取购物车的所有清单
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();
        if (shoppingCartList.size() == 0) {
            //没有购物清单
            return Result.error("");
        }
        //2.插入订单
        //设置订单数据
        setOrderInfo(order);
        if (save(order)) {
            Long id = order.getId();
            //3.插入订单详情数据
            for (ShoppingCart shoppingCart : shoppingCartList) {
                OrderDetail orderDetail = BeanUtils.copyProperty(new OrderDetail(), shoppingCart);
                orderDetail.setId(null);
                orderDetail.setOrderId(id);
                orderDetailService.save(orderDetail);
            }

            //清空购物车
            shoppingCartService.remove(new QueryWrapper<>());

            return Result.success(null);
        }
        return Result.error("下单失败");
    }

    /**
     * 再来一单
     *
     * @param order 订单信息 -- 只含订单id
     * @return 处理信息
     */
    @Transactional
    @Override
    public Result<Object> orderAgain(Orders order) {
        //查询订单
        Long orderId = order.getId();
        order = getById(orderId);
        if (order == null) {
            return Result.error("找不到订单信息");
        }
        //获取订单详细数据
        List<OrderDetail> orderDetails = orderDetailService.queryByOrderId(orderId);

        //设置新订单信息
        order.setId(null);
        order.setStatus(null);
        order.setNumber(StrUtils.createUUID(false));
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());

        if (save(order)) {
            //保存后的订单id
            orderId = order.getId();
            //插入订单详细信息
            for (OrderDetail orderDetail : orderDetails) {
                orderDetail.setId(null);
                orderDetail.setOrderId(orderId);
            }
            //插入订单详细信息
            orderDetailService.saveBatch(orderDetails);

            return Result.success(null);
        }

        return Result.error("下单失败！");
    }

    /**
     * 编辑订单状态
     *
     * @param order 封装了 订单id 目标状态值
     * @return 处理结果 成功/失败
     */
    @Override
    public Result<Object> editOrderStatus(Orders order) {
        Long orderId = order.getId();//id
        Integer status = order.getStatus();//状态

        if (orderId == null || status == null) {
            return Result.error("修改失败！");
        }
        LambdaUpdateWrapper<Orders> updateWrapper = Wrappers.lambdaUpdate(Orders.class)
                .eq(Orders::getId, orderId)
                .set(Orders::getStatus, status);
        //修改
        if (update(updateWrapper)) {
            //成功
            return Result.success(null);
        }
        //失败
        return Result.error("修改失败！");
    }


    private HashMap<String, Object> toOrderDtosMap(Page<Orders> ordersPage) {
        List<Orders> orders = ordersPage.getRecords();

        List<OrdersDto> ordersDtos = new ArrayList<>();

        for (Orders order : orders) {
            //转成DTO
            OrdersDto ordersDto = BeanUtils.toDTO(OrdersDto.class, order);
            //查询
            List<OrderDetail> orderDetails = orderDetailService.queryByOrderId(order.getId());
            //封装
            ordersDto.setOrderDetails(orderDetails);
            ordersDtos.add(ordersDto);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("records", ordersDtos);
        map.put("total", ordersPage.getTotal());
        return map;
    }

    /**
     * 设置订单信息
     *
     * @param order 订单
     */
    private void setOrderInfo(@NonNull Orders order) {
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();

        //总金额
        BigDecimal amount = new BigDecimal(0);
        for (ShoppingCart shoppingCart : shoppingCartList) {
            amount = amount.add(shoppingCart.getAmount());
        }
        order.setAmount(amount);
        //用户id、用户名
        order.setUserId(UserHolder.getUser().getId());
        order.setUserName(UserHolder.getUser().getName());
        //设置时间
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        //设置地址信息
        AddressBook addressBook = addressBookService.getById(order.getAddressBookId());
        order.setAddress(addressBook.getDetail());
        order.setPhone(addressBook.getPhone());
        order.setConsignee(addressBook.getConsignee());
        //设置订单编号
        String orderCode = StrUtils.createUUID(false);
        order.setNumber(orderCode);
    }
}

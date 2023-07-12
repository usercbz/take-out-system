package com.cbz.takeoutsystem.dto;

import com.cbz.takeoutsystem.entity.OrderDetail;
import com.cbz.takeoutsystem.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;

}

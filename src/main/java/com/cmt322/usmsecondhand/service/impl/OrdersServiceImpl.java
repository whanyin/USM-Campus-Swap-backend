package com.cmt322.usmsecondhand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmt322.usmsecondhand.model.Orders;
import com.cmt322.usmsecondhand.service.OrdersService;
import com.cmt322.usmsecondhand.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author 米老头
* @description 针对表【orders(Order Table)】的数据库操作Service实现
* @createDate 2025-11-22 14:08:33
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}







package com.lsq.springboot.test.mybatisplus.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lsq.springboot.test.mybatisplus.infrastruture.model.OrderPo;

public interface OrderService extends IService<OrderPo> {

    OrderPo addOrderForMaster();

    OrderPo addOrderForSlave();

    OrderPo findByOrderIdForMaster(long orderId);

    OrderPo findByOrderIdForSlave(long orderId);
}

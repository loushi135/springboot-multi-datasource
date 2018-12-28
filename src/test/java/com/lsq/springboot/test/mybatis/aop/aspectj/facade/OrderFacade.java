package com.lsq.springboot.test.mybatis.aop.aspectj.facade;

import com.lsq.springboot.test.mybatis.infrastruture.model.OrderPo;

public interface OrderFacade {

    OrderPo findByOrderIdForMaster(long orderId);

    OrderPo findByOrderIdForSlave(long orderId);

}

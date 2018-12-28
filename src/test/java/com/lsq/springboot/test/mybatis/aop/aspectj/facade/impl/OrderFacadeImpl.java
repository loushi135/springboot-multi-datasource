

package com.lsq.springboot.test.mybatis.aop.aspectj.facade.impl;

import com.lsq.springboot.datasource.starter.annotation.Slave;
import com.lsq.springboot.test.mybatis.aop.aspectj.facade.OrderFacade;
import com.lsq.springboot.test.mybatis.infrastruture.mapper.OrderMapper;
import com.lsq.springboot.test.mybatis.infrastruture.model.OrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderFacadeImpl implements OrderFacade {

    @Autowired
    private OrderMapper orderMapper;

    public OrderPo findByOrderIdForMaster(long orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

    @Slave
    public OrderPo findByOrderIdForSlave(long orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

}

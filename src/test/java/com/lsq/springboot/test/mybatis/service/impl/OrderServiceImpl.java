

package com.lsq.springboot.test.mybatis.service.impl;

import com.lsq.springboot.datasource.starter.annotation.DsGroup;
import com.lsq.springboot.datasource.starter.annotation.Slave;
import com.lsq.springboot.test.mybatis.SpelModel;
import com.lsq.springboot.test.mybatis.infrastruture.mapper.OrderMapper;
import com.lsq.springboot.test.mybatis.infrastruture.model.OrderPo;
import com.lsq.springboot.test.mybatis.infrastruture.query.OrderQuery;
import com.lsq.springboot.test.mybatis.service.OrderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@DsGroup("testorder")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderQuery orderQuery;

    private OrderPo insert() {
        OrderPo orderPo = new OrderPo();
        orderPo.setName(randomStringFixLength(10));
        orderPo.setTitle(randomStringFixLength(10));
        orderPo.setUserId(new Random().nextInt(20));
        orderMapper.insert(orderPo);
        return orderPo;
    }

    /**
     * 随机字母或数字，固定长度
     */
    public static String randomStringFixLength(int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null, threadLocalRandom());
    }

    /**
     * 返回无锁的ThreadLocalRandom
     */
    public static Random threadLocalRandom() {
        return java.util.concurrent.ThreadLocalRandom.current();
    }

    @Override
    public OrderPo addOrderForMaster() {
        return insert();
    }

    @Override
    @Slave
    public OrderPo addOrderForSlave() {
        return insert();
    }


    @Override
    public OrderPo findByOrderIdForMaster(long orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

    @Slave
    @Override
    public OrderPo findByOrderIdForSlave(long orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }


    @DsGroup("testorder1")
    @Override
    public OrderPo addOrder1ForMaster() {
        return insert();
    }

    @DsGroup("testorder1")
    @Slave
    @Override
    public OrderPo addOrder1ForSlave() {
        return insert();
    }

    @Override
    public OrderPo findByOrderId1ForMaster(long orderId) {

        return orderQuery.findAllForMaster().get(0);
    }

    @Override
    public OrderPo findByOrderId1ForSlave(long orderId) {
        return orderQuery.findAllForSlave().get(0);
    }


    @DsGroup("#dsGroup")
    @Slave(spel = "#slave")
    @Override
    public OrderPo findByOrderIdForSpel(long orderId, String dsGroup, boolean slave) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

    @DsGroup("#spelModel.dsGroup")
    @Slave(spel = "#spelModel.slave")
    @Override
    public OrderPo findByOrderIdForSpel(long orderId, SpelModel spelModel) {
        return orderMapper.selectByPrimaryKey(orderId);
    }
}

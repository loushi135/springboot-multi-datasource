/*
 * Copyright 2018 the organization loushi135
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.lsq.springboot.test.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsq.springboot.datasource.starter.annotation.DsGroup;
import com.lsq.springboot.datasource.starter.annotation.Slave;
import com.lsq.springboot.test.mybatisplus.infrastruture.mapper.OrderMapper;
import com.lsq.springboot.test.mybatisplus.infrastruture.model.OrderPo;
import com.lsq.springboot.test.mybatisplus.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.lsq.springboot.test.mybatis.service.impl.OrderServiceImpl.randomStringFixLength;

@Service
@DsGroup("testorder")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderPo> implements OrderService {

    private OrderPo insert() {
        OrderPo orderPo = new OrderPo();
        orderPo.setName(randomStringFixLength(10));
        orderPo.setTitle(randomStringFixLength(10));
        orderPo.setUserId(new Random().nextInt(20));

        save(orderPo);
        return orderPo;
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
        return baseMapper.selectById(orderId);
    }

    @Slave
    @Override
    public OrderPo findByOrderIdForSlave(long orderId) {
        return baseMapper.selectById(orderId);
    }

}

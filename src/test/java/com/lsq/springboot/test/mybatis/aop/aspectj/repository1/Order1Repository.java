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



package com.lsq.springboot.test.mybatis.aop.aspectj.repository1;

import com.lsq.springboot.datasource.starter.annotation.Slave;
import com.lsq.springboot.test.mybatis.infrastruture.mapper.OrderMapper;
import com.lsq.springboot.test.mybatis.infrastruture.model.OrderPo;
import com.lsq.springboot.test.mybatis.infrastruture.mapper.OrderMapper;
import com.lsq.springboot.test.mybatis.infrastruture.model.OrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Order1Repository {

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

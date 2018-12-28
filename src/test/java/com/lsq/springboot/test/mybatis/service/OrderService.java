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



package com.lsq.springboot.test.mybatis.service;

import com.lsq.springboot.test.mybatis.SpelModel;
import com.lsq.springboot.test.mybatis.infrastruture.model.OrderPo;
public interface OrderService {

    OrderPo addOrderForMaster();

    OrderPo addOrderForSlave();

    OrderPo findByOrderIdForMaster(long orderId);

    OrderPo findByOrderIdForSlave(long orderId);


    OrderPo addOrder1ForMaster();

    OrderPo addOrder1ForSlave();

    OrderPo findByOrderId1ForMaster(long orderId);

    OrderPo findByOrderId1ForSlave(long orderId);


    OrderPo findByOrderIdForSpel(long orderId,String dsGroup,boolean slave);

    OrderPo findByOrderIdForSpel(long orderId, SpelModel spelModel);

}

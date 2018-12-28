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



package com.lsq.springboot.test.mybatis.infrastruture.query;


import com.lsq.springboot.datasource.starter.annotation.DsGroup;
import com.lsq.springboot.datasource.starter.annotation.Slave;
import com.lsq.springboot.test.mybatis.infrastruture.model.OrderPo;

import java.util.List;

public interface OrderQuery {


    @DsGroup("testorder1")
    @Slave(isSlave = false)
    List<OrderPo> findAllForMaster();

    @DsGroup("testorder1")
    @Slave
    List<OrderPo> findAllForSlave();


}

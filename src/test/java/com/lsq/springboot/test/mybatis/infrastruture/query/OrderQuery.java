

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

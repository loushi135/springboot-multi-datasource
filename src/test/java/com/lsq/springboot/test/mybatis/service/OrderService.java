

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

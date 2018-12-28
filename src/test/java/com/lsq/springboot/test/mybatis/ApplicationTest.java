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



package com.lsq.springboot.test.mybatis;

import com.lsq.springboot.test.BaseTest;
import com.lsq.springboot.test.mybatis.aop.aspectj.facade.OrderFacade;
import com.lsq.springboot.test.mybatis.aop.aspectj.repository.OrderRepository;
import com.lsq.springboot.test.mybatis.aop.aspectj.repository1.Order1Repository;
import com.lsq.springboot.test.mybatis.infrastruture.model.OrderPo;
import com.lsq.springboot.test.mybatis.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest extends BaseTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private Order1Repository order1Repository;
    @Autowired
    private OrderFacade orderFacade;

    /**
     * 开启 sppring.datasource.multi.p6spy=true 会打印具体sql
     * p6spy KEY[] - 1545882694616|0|statement|connection 0|url
     * |select            id, name, title, user_id         from tb_order     where id = ?
     * |select            id, name, title, user_id         from tb_order     where id = 1
     */

    @Test
    public void addOrder() {

        //testorder主库新增orderpo
        OrderPo orderPoMaster = orderService.addOrderForMaster();
        //testorder从库新增orderpo
        OrderPo orderPoSlave = orderService.addOrderForSlave();

        //由于两个不同库，h2 db，自增长 ，id相同
        Assert.assertEquals(orderPoMaster.getId(), orderPoSlave.getId());

        //testorder主库查询orderpo
        OrderPo poMaster = orderService.findByOrderIdForMaster(orderPoMaster.getId());
        //testorder从库查询orderpo
        OrderPo poSlave = orderService.findByOrderIdForSlave(orderPoSlave.getId());

        //查询结果与新增一致
        Assert.assertEquals(orderPoMaster.getName(), poMaster.getName());
        Assert.assertEquals(orderPoSlave.getName(), poSlave.getName());

        //testorder1主库新增orderpo
        orderPoMaster = orderService.addOrder1ForMaster();
        //testorder1从库新增orderpo
        orderPoSlave = orderService.addOrder1ForSlave();
        Assert.assertEquals(orderPoMaster.getId(), orderPoSlave.getId());

        poMaster = orderService.findByOrderId1ForMaster(orderPoMaster.getId());
        poSlave = orderService.findByOrderId1ForSlave(orderPoSlave.getId());

        //查询结果与新增一致
        Assert.assertEquals(orderPoMaster.getName(), poMaster.getName());
        Assert.assertEquals(orderPoSlave.getName(), poSlave.getName());
    }


    //事务中 只会用master
    @Test
    public void testTransaction() {

        transactionTemplate.execute(transactionStatus -> {
            //testorder主库新增orderpo
            OrderPo orderPoMaster = orderService.addOrderForMaster();
            //testorder从库新增orderpo，由于在事务中，将使用主库
            OrderPo orderPoSlave = orderService.addOrderForSlave();

            //在主库新增，所以产生两个id
            Assert.assertEquals(1L, orderPoMaster.getId().longValue());
            Assert.assertEquals(2L, orderPoSlave.getId().longValue());

            //查询
            OrderPo poMaster = orderService.findByOrderIdForMaster(orderPoMaster.getId());
            OrderPo poSlave = orderService.findByOrderIdForSlave(orderPoSlave.getId());

            Assert.assertEquals(orderPoMaster.getName(), poMaster.getName());
            Assert.assertEquals(orderPoSlave.getName(), poSlave.getName());

            return null;
        });
    }


    @Test
    public void testSpel() {
        //testorder主库新增orderpo
        OrderPo orderPoMaster = orderService.addOrderForMaster();
        //testorder从库新增orderpo
        OrderPo orderPoSlave = orderService.addOrderForSlave();
        //testorder1主库新增orderpo
        OrderPo orderPoMaster1 = orderService.addOrder1ForMaster();
        //testorder1从库新增orderpo
        OrderPo orderPoSlave1 = orderService.addOrder1ForSlave();

        //spel 查询testorder组 master库
        OrderPo poMaster = orderService.findByOrderIdForSpel(orderPoMaster.getId(), "testorder", false);
        Assert.assertEquals(orderPoMaster.getId(), poMaster.getId());
        Assert.assertEquals(orderPoMaster.getName(), poMaster.getName());

        //spel 查询testorder组 slave库
        OrderPo poSlave = orderService.findByOrderIdForSpel(orderPoSlave.getId(), "testorder", true);
        Assert.assertEquals(orderPoSlave.getId(), poSlave.getId());
        Assert.assertEquals(orderPoSlave.getName(), poSlave.getName());


        //spel model 查询testorder1组 master库
        SpelModel spelModel = new SpelModel();
        spelModel.setDsGroup("testorder1");
        OrderPo poMaster1 = orderService.findByOrderIdForSpel(orderPoMaster1.getId(), spelModel);
        Assert.assertEquals(orderPoMaster1.getId(), poMaster1.getId());
        Assert.assertEquals(orderPoMaster1.getName(), poMaster1.getName());

        //spel model 查询testorder1组 slave库
        spelModel.setSlave(true);
        OrderPo poSlave1 = orderService.findByOrderIdForSpel(orderPoSlave1.getId(), spelModel);
        Assert.assertEquals(orderPoSlave1.getId(), poSlave1.getId());
        Assert.assertEquals(orderPoSlave1.getName(), poSlave1.getName());
    }


    @Test
    public void testAop() {
        //testorder主库新增orderpo
        OrderPo orderPoMaster = orderService.addOrderForMaster();
        //testorder从库新增orderpo
        OrderPo orderPoSlave = orderService.addOrderForSlave();
        //testorder1主库新增orderpo
        OrderPo orderPoMaster1 = orderService.addOrder1ForMaster();
        //testorder1从库新增orderpo
        OrderPo orderPoSlave1 = orderService.addOrder1ForSlave();


        //testorder主库查询
        OrderPo poMaster = orderFacade.findByOrderIdForMaster(orderPoMaster.getId());
        Assert.assertEquals(orderPoMaster.getId(), poMaster.getId());
        Assert.assertEquals(orderPoMaster.getName(), poMaster.getName());

        //testorder从库查询
        OrderPo poSlave = orderFacade.findByOrderIdForSlave(orderPoMaster.getId());
        Assert.assertEquals(orderPoSlave.getId(), poSlave.getId());
        Assert.assertEquals(orderPoSlave.getName(), poSlave.getName());

        //testorder主库查询
        poMaster = orderRepository.findByOrderIdForMaster(orderPoMaster.getId());
        Assert.assertEquals(orderPoMaster.getId(), poMaster.getId());
        Assert.assertEquals(orderPoMaster.getName(), poMaster.getName());

        //testorder从库查询
        poSlave = orderRepository.findByOrderIdForSlave(orderPoMaster.getId());
        Assert.assertEquals(orderPoSlave.getId(), poSlave.getId());
        Assert.assertEquals(orderPoSlave.getName(), poSlave.getName());

        //testorder1主库查询
        poMaster = order1Repository.findByOrderIdForMaster(orderPoMaster.getId());
        Assert.assertEquals(orderPoMaster1.getId(), poMaster.getId());
        Assert.assertEquals(orderPoMaster1.getName(), poMaster.getName());

        //testorder1从库查询
        poSlave = order1Repository.findByOrderIdForSlave(orderPoMaster.getId());
        Assert.assertEquals(orderPoSlave1.getId(), poSlave.getId());
        Assert.assertEquals(orderPoSlave1.getName(), poSlave.getName());
    }
}



package com.lsq.springboot.test.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsq.springboot.test.mybatisplus.infrastruture.model.OrderPo;
import com.lsq.springboot.test.BaseTest;
import com.lsq.springboot.test.mybatisplus.infrastruture.model.OrderPo;
import com.lsq.springboot.test.mybatisplus.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest extends BaseTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private OrderService orderService;


    @Test
    public void addOrder() {

        OrderPo orderPoMaster = orderService.addOrderForMaster();
        OrderPo orderPoSlave = orderService.addOrderForSlave();

        Assert.assertEquals(orderPoMaster.getId(), orderPoSlave.getId());

        OrderPo poMaster = orderService.findByOrderIdForMaster(orderPoMaster.getId());
        OrderPo poSlave = orderService.findByOrderIdForSlave(orderPoSlave.getId());


        Assert.assertEquals(orderPoMaster.getName(), poMaster.getName());
        Assert.assertEquals(orderPoSlave.getName(), poSlave.getName());
    }


    @Test
    public void testPage(){
        OrderPo orderPoMaster1 = orderService.addOrderForMaster();
        OrderPo orderPoMaster2 = orderService.addOrderForMaster();
        OrderPo orderPoMaster3 = orderService.addOrderForMaster();
        OrderPo orderPoMaster4 = orderService.addOrderForMaster();
        OrderPo orderPoMaster5 = orderService.addOrderForMaster();


        IPage<OrderPo> page = new Page<>(1,2);

        QueryWrapper<OrderPo> wrapper = Wrappers.query();
        wrapper.lt("id",100);

        IPage<OrderPo> poPage = orderService.page(page,wrapper);
        Assert.assertEquals(5,poPage.getTotal());
        Assert.assertEquals(2,poPage.getRecords().size());
        Assert.assertEquals(1,poPage.getCurrent());
        Assert.assertEquals(poPage.getRecords().get(0).getName(),orderPoMaster1.getName());
        Assert.assertEquals(poPage.getRecords().get(1).getName(),orderPoMaster2.getName());

        page = new Page<>(2,2);
        poPage = orderService.page(page,Wrappers.emptyWrapper());
        Assert.assertEquals(5,poPage.getTotal());
        Assert.assertEquals(2,poPage.getRecords().size());
        Assert.assertEquals(2,poPage.getCurrent());
        Assert.assertEquals(poPage.getRecords().get(0).getName(),orderPoMaster3.getName());
        Assert.assertEquals(poPage.getRecords().get(1).getName(),orderPoMaster4.getName());

        page = new Page<>(3,2);
        poPage = orderService.page(page,Wrappers.emptyWrapper());
        Assert.assertEquals(5,poPage.getTotal());
        Assert.assertEquals(1,poPage.getRecords().size());
        Assert.assertEquals(3,poPage.getCurrent());
        Assert.assertEquals(poPage.getRecords().get(0).getName(),orderPoMaster5.getName());

    }

}



package com.lsq.springboot.test.mybatis.infrastruture.mapper;


import com.lsq.springboot.test.mybatis.infrastruture.model.OrderExample;
import com.lsq.springboot.test.mybatis.infrastruture.model.OrderPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int countByExample(OrderExample example);

    int deleteByExample(OrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderPo record);

    int insertSelective(OrderPo record);

    List<OrderPo> selectByExample(OrderExample example);

    OrderPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderPo record, @Param("example") OrderExample example);

    int updateByExample(@Param("record") OrderPo record, @Param("example") OrderExample example);

    int updateByPrimaryKeySelective(OrderPo record);

    int updateByPrimaryKey(OrderPo record);
}
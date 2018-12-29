# springboot-multi-datasource
springboot多数据源的Starter

### 简介
主要代码都来自于[dynamic-datasource-spring-boot-starter](https://github.com/baomidou/dynamic-datasource-spring-boot-starter)

主要改动：
- application.properties 前缀: spring.datasource.multi
- 数据库分组，比如订单库组 spring.datasource.multi.group.order
- 每个数据库组下分一个主库（master）和多个从库（slaves）
   ```
      spring.datasource.multi.group.order.master.url=xxx
      spring.datasource.multi.group.order.slaves[0].url=xxx
      spring.datasource.multi.group.order.slaves[1].url=xxx
   ```
- 通过注解@DsGroup动态指定数据库组
- 通过注解@Slave动态指定是否使用从库
- 在事务中只会使用主库     


### 使用方法

1. 依赖multi-datasource-spring-boot-starter

```xml
<dependency>
  <groupId>io.github.loushi135</groupId>
  <artifactId>multi-datasource-spring-boot-starter</artifactId>
  <version>${version}</version>
</dependency>
```

2. 配置数据源组
```properties
#mybatis starter配置
mybatis.check-config-location=true
mybatis.config-location=classpath:mybatis-settings.xml
mybatis.mapper-locations=classpath*:sqlmap/**/*.xml

#全局配置druid配置
sppring.datasource.multi.druid.initial-size=2
sppring.datasource.multi.druid.max-active=10
sppring.datasource.multi.druid.query-timeout=30
sppring.datasource.multi.p6spy=true

#组testorder master连接池配置
sppring.datasource.multi.group.testorder.master.primary=true
sppring.datasource.multi.group.testorder.master.driver-class-name=org.h2.Driver
sppring.datasource.multi.group.testorder.master.username=sa
sppring.datasource.multi.group.testorder.master.password=""
sppring.datasource.multi.group.testorder.master.url=jdbc:h2:mem:testorder
#master连接池参数initial-size,将使用此值
sppring.datasource.multi.group.testorder.master.druid.initial-size=5

#组testorder下druid配置
sppring.datasource.multi.group.testorder.druid.initial-size=3
sppring.datasource.multi.group.testorder.druid.min-idle=1
sppring.datasource.multi.group.testorder.druid.max-active=10
sppring.datasource.multi.group.testorder.druid.max-wait=60000
sppring.datasource.multi.group.testorder.druid.time-between-eviction-runs-millis=60000
sppring.datasource.multi.group.testorder.druid.min-evictable-idle-time-millis=300000
sppring.datasource.multi.group.testorder.druid.validation-query=select 'x'
sppring.datasource.multi.group.testorder.druid.test-while-idle=true
sppring.datasource.multi.group.testorder.druid.test-on-borrow=false
sppring.datasource.multi.group.testorder.druid.validation-query-timeout=30
sppring.datasource.multi.group.testorder.druid.transaction-query-timeout=60

#组testorder下slave连接池
sppring.datasource.multi.group.testorder.slaves[0].driver-class-name=org.h2.Driver
sppring.datasource.multi.group.testorder.slaves[0].username=sa
sppring.datasource.multi.group.testorder.slaves[0].password=""
sppring.datasource.multi.group.testorder.slaves[0].url=jdbc:h2:mem:testorder-slave0

#组testorder1 master连接池配置
sppring.datasource.multi.group.testorder1.master.driver-class-name=org.h2.Driver
sppring.datasource.multi.group.testorder1.master.username=sa
sppring.datasource.multi.group.testorder1.master.password=""
sppring.datasource.multi.group.testorder1.master.url=jdbc:h2:mem:testorder1
sppring.datasource.multi.group.testorder1.master.druid.initial-size=5

#组testorder1 slave连接池配置
sppring.datasource.multi.group.testorder1.slaves[0].driver-class-name=org.h2.Driver
sppring.datasource.multi.group.testorder1.slaves[0].username=sa
sppring.datasource.multi.group.testorder1.slaves[0].password=""
sppring.datasource.multi.group.testorder1.slaves[0].url=jdbc:h2:mem:testorder1-slave0
```
前缀: sppring.datasource.multi 

一级配置（MultiDataSourceProperties类下属性）：

    p6spy:  是否使用p6spy输出，默认不输出, 开发测试环境可配置为true,用于打印可执行的sql,
    eg:    p6spy KEY[] - 1545882694616|0|statement|connection 0|url|select id, name, title, user_id from tb_order where id = ?|select id, name, title, user_id from tb_order where id = 1    
    druid:  全局druid配置，可以统一配置连接池参数，具体参数配置详见DruidConfig
    
    group:   数据源分组 Map ,key为组名（一般设置为数据库名） value为类型： DataSourceGroupProperties

二级配置（DataSourceGroupProperties类下属性）：
 
    master:  master数据库配置，类型：DataSourceProperty
    
    slaves:  多个slave数据库配置
    
    druid:   此组下druid默认配置
    
三级配置（DataSourceProperty类下属性）：

    primary: 主否作为默认数据库连接池（用于没有配置@DsGroup）默认使用的连接池，可不配（默认使用第一个组的master）
    driverClassName: JDBC DRIVER 
    url:             JDBC URL地址
    username:        JDBC 用户名
    password:        JDBC 密码
    druid:           DruidConfig 此连接池参数配置，优先使用此配置，
                     如果对应属性没有配置 则使用此group下druid配置 ,
                     group没有配置则使用全局druid配置，
                     再没有的话使用 默认参数配置,默认配置如下DataSourceSettingEnum           
```java
public enum DataSourceSettingEnum {

    initialSize("20"),

    minIdle("5"),

    maxActive("100"),

    /**
     * 配置获取连接等待超时的时间
     */
    maxWait("60000"),
    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    timeBetweenEvictionRunsMillis("60000"),

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    minEvictableIdleTimeMillis("300000"),

    validationQuery("SELECT 'x'"),

    testWhileIdle("true"),

    testOnBorrow("false"),
    /**
     * datasource 单位秒
     * 查询timeout throws SQLException;
     */
    queryTimeout("30");
}
```    
### 注解定义

- DsGroup 用于确定使用什么数据库组
```java
/**
 * 可用于类和方法
 * 设置数据库组
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DsGroup {

    /**
     * database group name，support spel determine by params args
     * <pre> {@code
     * @DsGroup("testorder")
     * public class OrderServiceImpl extends OrderService {
     *
     *   using slave for group testorder1
     *   @DsGroup("testorder1")
     *   @Slave
     *   @Override
     *   public OrderPo addOrderForSlave() {
     *     return insert();
     *   }
     *
     *   default using master for group testorder
     *   @Override
     *   public OrderPo addOrder() {
     *     return insert();
     *   }
     *
     *   depends on params args to determine group and master or slave
     *   @DsGroup("#dsGroup")
     *   @Slave("#slave")
     *   @Override
     *   public OrderPo addOrderForSpel(String dsGroup,boolean slave) {
     *     return insert();
     *   }
     *
     * }}</pre>
     *
     * @return 数据源名称
     */
    String value();
}

```

- Slave 用于确定是否使用从库
```java
/**
 * 可用于类和方法
 * 是否使用从库
 * 不带@Slave注解使用主库，
 * 带@Slave注解如果 isSlave为false也使用主库
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Slave {
    boolean isSlave() default true;

    /**
     * 通过参数来定是否slave
     *
     * @return
     */
    String spel() default "";
}

// spel文档如下：
// https://docs.spring.io/spring-framework/docs/5.1.3.RELEASE/spring-framework-reference/core.html#expressions-evaluation
// https://docs.spring.io/spring-framework/docs/5.1.3.RELEASE/spring-framework-reference/core.html#expressions-language-ref
```
- AOP,通过不同包路径设置不同数据库组
```java
    @Bean
    public MultiDataSourceMatcherConfig multiDataSourceMatcherConfig() {
        return MultiDataSourceMatcherConfig.newInstance()
                .expressionMatcher("execution(* com.lsq.springboot.test.mybatis.aop.aspectj.facade.*Facade.*(..))", "testorder")
                .expressionMatcher("execution(* com.lsq.springboot.test.mybatis.aop.aspectj.repository.*Repository.*(..))", "testorder")
                .expressionMatcher("execution(* com.lsq.springboot.test.mybatis.aop.aspectj.repository1.*Repository.*(..))", "testorder1")
                ;
    }
    //如上所示 facade类和repository下都使用testorder组, repository1下都使用testorder1组
```



###Sample
详见test目录下单元测试
- [Mybatis-Test](https://github.com/loushi135/springboot-multi-datasource/blob/master/src/test/java/com/lsq/springboot/test/mybatis/ApplicationTest.java)
   
   测试准备
   
   - OrderFacade、OrderRepository 、Order1Repository定义
   ```java
    @Component
    public class OrderFacadeImpl implements OrderFacade {
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
      
    @Component
    public class OrderRepository {
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
    
   ```
   - OrderService默认使用testorder数据库
   ```java
    @Service
    @DsGroup("testorder")
    public class OrderServiceImpl implements OrderService {
        @Autowired
        private OrderMapper orderMapper;
        @Autowired
        private OrderQuery orderQuery;
        private OrderPo insert() {
            OrderPo orderPo = new OrderPo();
            orderPo.setName(RandomUtil.randomStringFixLength(10));
            orderPo.setTitle(RandomUtil.randomStringFixLength(10));
            orderPo.setUserId(RandomUtil.nextInt(20));
            orderMapper.insert(orderPo);
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
   ```
   - OrderMapper为mybatis generator自动生成
   - OrderQuery 使用testorder1 数据库
   ```java
    public interface OrderQuery {
        @DsGroup("testorder1")
        @Slave(isSlave = false)
        List<OrderPo> findAllForMaster();
     
        @DsGroup("testorder1")
        @Slave
        List<OrderPo> findAllForSlave();
    }
   ```
   
   测试主从多数据源新增查询
   
   ```java
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
  
   ```
   
   测试事务 事务中 只会用master
   ```java
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
   ```
   
   spel表达式测试，通过动态参数选择不同数据库组和主从库
   
   ```java
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
   ```
   
   AOP测试，不同包对应不同数据库组
   ```java
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
   ```
   
- [Mybatis-Plus-Test](https://github.com/loushi135/springboot-multi-datasource/blob/master/src/test/java/com/lsq/springboot/test/mybatisplus/ApplicationTest.java)

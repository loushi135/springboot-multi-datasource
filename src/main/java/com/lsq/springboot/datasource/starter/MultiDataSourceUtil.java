package com.lsq.springboot.datasource.starter;

import com.lsq.springboot.datasource.starter.annotation.DsGroup;
import com.lsq.springboot.datasource.starter.annotation.Slave;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

public class MultiDataSourceUtil {

    public static String determineDsGroupFromAnnontation(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class<?> declaringClass = invocation.getThis().getClass();
        DsGroup ds = method.isAnnotationPresent(DsGroup.class) ? method.getAnnotation(DsGroup.class)
                : AnnotationUtils.findAnnotation(declaringClass, DsGroup.class);
        String dsGroup = ds.value();

        //spring spel 表达式
        if (SpelUtil.isSpel(dsGroup)) {
            dsGroup = SpelUtil.parseSpel(dsGroup, invocation);
        }
        return dsGroup;
    }


    public static String determineMasterSlave(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class<?> declaringClass = invocation.getThis().getClass();
        Slave slave = method.isAnnotationPresent(Slave.class) ? method.getAnnotation(Slave.class)
                : AnnotationUtils.findAnnotation(declaringClass, Slave.class);

        //@Slave注解没有 或者在事务中，设置为MASTER
        if (null == slave || isExistingTransaction()) {
            return DataSourceTypeEnum.MASTER.name();
        }

        boolean isSlave = slave.isSlave();

        if (StringUtils.isNotBlank(slave.spel()) && SpelUtil.isSpel(slave.spel())) {
            isSlave = Boolean.valueOf(SpelUtil.parseSpel(slave.spel(), invocation));
        }

        return isSlave ? DataSourceTypeEnum.SLAVE.name() : DataSourceTypeEnum.MASTER.name();
    }

    /**
     * 当前是否在事务中
     *
     * @return
     */
    private static boolean isExistingTransaction() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }

}

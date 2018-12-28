package com.lsq.springboot.datasource.starter.aspect;


import com.lsq.springboot.datasource.starter.MultiDataSourceContextHolder;
import com.lsq.springboot.datasource.starter.MultiDataSourceUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import static com.lsq.springboot.datasource.starter.MultiRoutingDataSource.SPLITOR;

public class MultiDataSourceAnnotationInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            MultiDataSourceContextHolder.setDataSourceLookupKey(determineDatasource(invocation));
            return invocation.proceed();
        } finally {
            MultiDataSourceContextHolder.clearDataSourceLookupKey();
        }
    }

    private String determineDatasource(MethodInvocation invocation) {

        String dsGroup = MultiDataSourceUtil.determineDsGroupFromAnnontation(invocation);
        String dataSourceType = MultiDataSourceUtil.determineMasterSlave(invocation);

        return dsGroup.toLowerCase() + SPLITOR + dataSourceType;
    }

}
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

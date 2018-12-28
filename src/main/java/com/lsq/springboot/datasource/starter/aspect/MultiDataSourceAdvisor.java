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

import com.google.common.collect.Maps;
import com.lsq.springboot.datasource.starter.MultiDataSourceContextHolder;
import com.lsq.springboot.datasource.starter.MultiDataSourceUtil;
import com.lsq.springboot.datasource.starter.aspect.matcher.ExpressionMatcher;
import com.lsq.springboot.datasource.starter.aspect.matcher.Matcher;
import com.lsq.springboot.datasource.starter.aspect.matcher.RegexMatcher;
import com.lsq.springboot.datasource.starter.aspect.pointcut.DsGroupAspectJExpressionPointcut;
import com.lsq.springboot.datasource.starter.aspect.pointcut.DsGroupJdkRegexpMethodPointcut;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.lsq.springboot.datasource.starter.MultiRoutingDataSource.SPLITOR;

/**
 * 用于通过不同切面表达式指定DsGroup
 */
public class MultiDataSourceAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private Advice advice;

    private Pointcut pointcut;

    private Map<String, String> matchesCache = Maps.newHashMap();

    public MultiDataSourceAdvisor(List<Matcher> matchers) {
        this.pointcut = buildPointcut(matchers);
        this.advice = buildAdvice();
    }

    private Advice buildAdvice() {
        return new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                try {
                    MultiDataSourceContextHolder.setDataSourceLookupKey(determineDatasource(invocation));
                    return invocation.proceed();
                } finally {
                    MultiDataSourceContextHolder.clearDataSourceLookupKey();
                }
            }
        };
    }

    private String determineDatasource(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class<?> declaringClass = invocation.getThis().getClass();
        String methodPath = declaringClass.getName() + "." + method.getName();

        String dsGroup = matchesCache.get(methodPath);
        String dataSourceType = MultiDataSourceUtil.determineMasterSlave(invocation);

        return dsGroup.toLowerCase() + SPLITOR + dataSourceType;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut(List<Matcher> matchers) {
        ComposablePointcut composablePointcut = null;
        for (Matcher matcher : matchers) {
            if (matcher instanceof RegexMatcher) {
                RegexMatcher regexMatcher = (RegexMatcher) matcher;
                Pointcut pointcut = new DsGroupJdkRegexpMethodPointcut(regexMatcher.getPattern(), regexMatcher.getDsGroup(), matchesCache);
                if (composablePointcut == null) {
                    composablePointcut = new ComposablePointcut(pointcut);
                } else {
                    composablePointcut.union(pointcut);
                }
            } else {
                ExpressionMatcher expressionMatcher = (ExpressionMatcher) matcher;
                Pointcut pointcut = new DsGroupAspectJExpressionPointcut(expressionMatcher.getExpression(), expressionMatcher.getDsGroup(), matchesCache);
                if (composablePointcut == null) {
                    composablePointcut = new ComposablePointcut(pointcut);
                } else {
                    composablePointcut.union(pointcut);
                }
            }
        }
        return composablePointcut;
    }

    @Override
    public void setOrder(int order) {
        super.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}

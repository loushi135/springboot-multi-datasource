package com.lsq.springboot.datasource.starter.aspect.pointcut;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;
import java.util.Map;

public class DsGroupAspectJExpressionPointcut extends AspectJExpressionPointcut {
    private Map<String, String> matchesCache;

    private String dsGroup;

    public DsGroupAspectJExpressionPointcut(String expression, String dsGroup, Map<String, String> matchesCache) {
        this.dsGroup = dsGroup;
        this.matchesCache = matchesCache;
        setExpression(expression);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, boolean beanHasIntroductions) {
        boolean matches = super.matches(method, targetClass, beanHasIntroductions);
        if (matches) {
            matchesCache.put(targetClass.getName() + "." + method.getName(), dsGroup);
        }
        return matches;
    }
}
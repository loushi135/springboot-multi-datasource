package com.lsq.springboot.datasource.starter.aspect.pointcut;

import org.springframework.aop.support.JdkRegexpMethodPointcut;

import java.util.Map;

public class DsGroupJdkRegexpMethodPointcut extends JdkRegexpMethodPointcut {

    private Map<String, String> matchesCache;

    private String dsGroup;

    public DsGroupJdkRegexpMethodPointcut(String pattern, String dsGroup, Map<String, String> matchesCache) {
        this.dsGroup = dsGroup;
        this.matchesCache = matchesCache;
        setPattern(pattern);
    }

    @Override
    protected boolean matches(String pattern, int patternIndex) {
        boolean matches = super.matches(pattern, patternIndex);
        if (matches) {
            matchesCache.put(pattern, dsGroup);
        }
        return matches;
    }

}

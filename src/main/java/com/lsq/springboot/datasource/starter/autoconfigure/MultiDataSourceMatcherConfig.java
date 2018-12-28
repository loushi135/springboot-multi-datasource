package com.lsq.springboot.datasource.starter.autoconfigure;

import com.google.common.collect.Lists;
import com.lsq.springboot.datasource.starter.aspect.matcher.ExpressionMatcher;
import com.lsq.springboot.datasource.starter.aspect.matcher.Matcher;
import com.lsq.springboot.datasource.starter.aspect.matcher.RegexMatcher;

import java.util.List;

public class MultiDataSourceMatcherConfig {

    private List<Matcher> matchers = Lists.newArrayList();

    public static MultiDataSourceMatcherConfig newInstance() {
        return new MultiDataSourceMatcherConfig();
    }

    public MultiDataSourceMatcherConfig regexMatcher(String pattern, String dsGroup) {
        matchers.add(new RegexMatcher(pattern, dsGroup));
        return this;
    }

    public MultiDataSourceMatcherConfig expressionMatcher(String pattern, String dsGroup) {
        matchers.add(new ExpressionMatcher(pattern, dsGroup));
        return this;
    }

    public List<Matcher> getMatchers() {
        return matchers;
    }

    public void setMatchers(List<Matcher> matchers) {
        this.matchers = matchers;
    }
}

package com.lsq.springboot.datasource.starter.aspect.matcher;

public class RegexMatcher implements Matcher {

    private String pattern;

    private String dsGroup;

    public RegexMatcher() {
    }

    public RegexMatcher(String pattern, String dsGroup) {
        this.pattern = pattern;
        this.dsGroup = dsGroup;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDsGroup() {
        return dsGroup;
    }

    public void setDsGroup(String dsGroup) {
        this.dsGroup = dsGroup;
    }
}

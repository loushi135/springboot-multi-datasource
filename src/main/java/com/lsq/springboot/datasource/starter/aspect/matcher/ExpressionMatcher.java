package com.lsq.springboot.datasource.starter.aspect.matcher;

public class ExpressionMatcher implements Matcher {

    private String expression;

    private String dsGroup;

    public ExpressionMatcher() {
    }

    public ExpressionMatcher(String expression, String dsGroup) {
        this.expression = expression;
        this.dsGroup = dsGroup;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDsGroup() {
        return dsGroup;
    }

    public void setDsGroup(String dsGroup) {
        this.dsGroup = dsGroup;
    }
}

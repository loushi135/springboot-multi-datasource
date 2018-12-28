package com.lsq.springboot.datasource.starter;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * spel
 *
 * @link https://docs.spring.io/spring-framework/docs/5.1.3.RELEASE/spring-framework-reference/core.html#expressions-evaluation
 * @link https://docs.spring.io/spring-framework/docs/5.1.3.RELEASE/spring-framework-reference/core.html#expressions-language-ref
 */
public class SpelUtil {

    /**
     * SPEL参数标识
     */
    private static final String SPEL_PREFIX = "#";

    /**
     * 参数发现器
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * Express语法解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();


    /**
     * 是否spel表达式
     *
     * @param key
     * @return
     */
    public static boolean isSpel(String key) {
        return key.startsWith(SPEL_PREFIX);
    }

    /**
     * parse
     *
     * @param key
     * @param invocation
     * @return
     */
    public static String parseSpel(String key, MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, arguments, NAME_DISCOVERER);
        return PARSER.parseExpression(key).getValue(context).toString();
    }
}

package com.lsq.springboot.datasource.starter.aspect;

import com.lsq.springboot.datasource.starter.annotation.DsGroup;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.lang.NonNull;

public class MultiDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private static final long serialVersionUID = -3358016326451923680L;
    private Advice advice;

    private Pointcut pointcut;

    public MultiDataSourceAnnotationAdvisor(@NonNull MultiDataSourceAnnotationInterceptor multiDataSourceAnnotationInterceptor) {
        this.advice = multiDataSourceAnnotationInterceptor;
        this.pointcut = buildPointcut();
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

    private Pointcut buildPointcut() {
        Pointcut cpc = new AnnotationMatchingPointcut(DsGroup.class, true);
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(DsGroup.class);
        return new ComposablePointcut(cpc).union(mpc);
    }
}

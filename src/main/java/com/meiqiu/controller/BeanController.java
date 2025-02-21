package com.meiqiu.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.NamedBeanHolder;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.core.ResolvableType;

import java.util.Set;

/**
 * @Description
 * @Author sgh
 * @Date 2025/2/21
 * @Time 10:29
 */
public class BeanController extends AbstractAutowireCapableBeanFactory {


    @Override
    public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> aClass) throws BeansException {
        return null;
    }

    @Override
    public Object resolveDependency(DependencyDescriptor dependencyDescriptor, String s, Set<String> set, TypeConverter typeConverter) throws BeansException {
        return null;
    }

    @Override
    protected boolean containsBeanDefinition(String s) {
        return false;
    }

    @Override
    protected BeanDefinition getBeanDefinition(String s) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> aClass) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> aClass, Object... objects) throws BeansException {
        return null;
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType) {
        return null;
    }
}

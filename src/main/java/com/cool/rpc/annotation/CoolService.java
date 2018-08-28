package com.cool.rpc.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * cool rpc server annotation (must)
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface CoolService {

    Class<? extends RpcService> value();

}

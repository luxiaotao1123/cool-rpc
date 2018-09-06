package com.cool.rpc;

import com.cool.rpc.center.ServiceCenter;
import java.lang.reflect.Proxy;

public class CoolProxy implements com.cool.rpc.Proxy {

    private ServiceCenter serviceDiscovery;

    public CoolProxy(ServiceCenter serviceDiscovery){
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> cls){
        return (T)Proxy.newProxyInstance(cls.getClassLoader(),
                new Class<?>[]{cls},
                new CoolInvocationHandler(serviceDiscovery, cls));

    }
}


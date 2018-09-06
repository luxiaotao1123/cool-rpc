package com.cool.rpc;

import com.cool.rpc.center.ServiceCenter;
import com.cool.rpc.protocol.CoolRequest;
import com.cool.rpc.protocol.CoolResponse;
import com.cool.rpc.util.LongFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class CoolProxy {

    private static Logger log = LoggerFactory.getLogger(CoolProxy.class);

    private ServiceCenter serviceDiscovery;

    public CoolProxy(ServiceCenter serviceDiscovery){
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> cls){

        return (T)Proxy.newProxyInstance(cls.getClassLoader(),
                new Class<?>[]{cls},
                (proxy, method, args) -> {

                    CoolRequest request = new CoolRequest();
                    request.setRequestId(LongFactory.getInstance().incrementAndGet());
                    request.setClassName(method.getDeclaringClass().getName());
                    request.setMethodName(method.getName());
                    request.setParameters(args);
                    request.setParameterTypes(method.getParameterTypes());

                    String[] addr = serviceDiscovery.discover(cls.getName()).split(":",2);

                    CoolRpcClient client = new CoolRpcClient(addr[0],
                            Integer.parseInt(addr[1]));

                    CoolResponse response = client.send(request);
                    if (response.getError()!=null){
                        throw response.getError();
                    } else {
                        return response.getResult();
                    }

                });
    }

}

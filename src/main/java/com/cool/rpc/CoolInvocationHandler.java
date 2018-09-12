package com.cool.rpc;

import com.cool.rpc.center.ServiceCenter;
import com.cool.rpc.pool.ChannelPool;
import com.cool.rpc.protocol.CoolRequest;
import com.cool.rpc.protocol.CoolResponse;
import com.cool.rpc.util.ChannelTools;
import com.cool.rpc.util.LongFactory;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class CoolInvocationHandler<T> implements InvocationHandler {

    private ServiceCenter serviceDiscovery;
    private Class<T> cls;

    public CoolInvocationHandler(){
    }

    public CoolInvocationHandler(ServiceCenter serviceDiscovery, Class<T> cls){
        this.serviceDiscovery = serviceDiscovery;
        this.cls = cls;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CoolRequest request = new CoolRequest();
        request.setRequestId(LongFactory.getInstance().incrementAndGet());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());

        if (serviceDiscovery.discover(cls.getName()) == null){
            throw new CoolException("The rpc service not exits");
        }
        String[] addr = serviceDiscovery.discover(cls.getName()).split(":",2);
        String serverIp = addr[0];
        int port = Integer.parseInt(addr[1]);

        CallBack callBack = new CallBack();

        ChannelPool channelPool = ChannelPool.newChannelPool(serverIp, port,5);
        Channel channel = channelPool.getChannel();
        ChannelTools.putData(channel, request.getRequestId(), callBack);
        channel.writeAndFlush(request);
        callBack.getCountDownLatch().await();

        CoolResponse response = (CoolResponse) callBack.getResult();
        if (response.getError() != null){
            throw response.getError();
        } else {
            return response.getResult();
        }

    }


}

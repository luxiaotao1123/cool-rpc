package com.cool.rpc.handler;

import com.cool.rpc.CoolRpcServer;
import com.cool.rpc.protocol.CoolRequest;
import com.cool.rpc.protocol.CoolResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * cool rpc server handler
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
@ChannelHandler.Sharable
public class CoolServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(CoolServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CoolResponse response = new CoolResponse();
        CoolRequest request = (CoolRequest) msg;

        try {
            Object result = invoke(request);
            response.setRequestID(request.getRequestID());
            response.setResult(result);
        } catch (Throwable error) {
            response.setError(error);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    private Object invoke(CoolRequest request) throws Throwable{
        if (request == null){
            throw new Throwable("cool rpc request not found");
        }

        String className = request.getClassName();
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        Object service = CoolRpcServer.servicesMap.get(className);
        if (service == null){
            throw new Throwable("cool rpc service not exist");
        }

        Class<?> serviceClass = service.getClass();
        Class<?>[] parameterTypes = request.getParameterTypes();

        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);
        return fastMethod.invoke(service, parameters);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server caught exception", cause);
        ctx.close();
    }

}

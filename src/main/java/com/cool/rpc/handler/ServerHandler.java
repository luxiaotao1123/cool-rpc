package com.cool.rpc.handler;

import com.cool.rpc.CoolException;
import com.cool.rpc.RpcServer;
import com.cool.rpc.protocol.CoolRequest;
import com.cool.rpc.protocol.CoolResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CoolResponse response = new CoolResponse();
        CoolRequest request = (CoolRequest) msg;

        try {
            Object result = invoke(request);
            response.setRequestId(request.getRequestId());
            response.setResult(result);
        } catch (Throwable error) {
            response.setError(error);
        }
        ctx.writeAndFlush(response);
    }


    private Object invoke(CoolRequest request) throws Throwable{
        if (request == null){
            throw new CoolException("Cool rpc request not found");
        }

        String className = request.getClassName();
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        Object service = RpcServer.servicesMap.get(className);
        if (service == null){
            throw new CoolException("Cool rpc service not exist");
        }

        Class<?> serviceClass = service.getClass();
        Class<?>[] parameterTypes = request.getParameterTypes();

        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);
        return fastMethod.invoke(service, parameters);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        throw new CoolException(cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE == e.state()) {
                ctx.close();
            }
        }

    }
}

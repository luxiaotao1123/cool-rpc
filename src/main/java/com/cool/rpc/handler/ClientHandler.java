package com.cool.rpc.handler;

import com.cool.rpc.CallBack;
import com.cool.rpc.CoolException;
import com.cool.rpc.protocol.CoolResponse;
import com.cool.rpc.util.ChannelTools;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        CoolResponse response = (CoolResponse) msg;
        CallBack callBack = ChannelTools.getRemove(channel, response.getRequestId());
        callBack.receiveMessage(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        throw new CoolException(cause);
    }
}

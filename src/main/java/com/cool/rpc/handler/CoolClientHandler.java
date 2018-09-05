package com.cool.rpc.handler;

import com.cool.rpc.protocol.CoolResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;


@ChannelHandler.Sharable
public class CoolClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(CoolClientHandler.class);

    private CountDownLatch latch;
    private CoolResponse response;

    public CoolClientHandler(CountDownLatch latch, CoolResponse response){
        this.latch = latch;
        this.response = response;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CoolResponse enResponse = (CoolResponse) msg;
        this.response.sync(enResponse);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        latch.countDown();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("api caught exception", cause);
        ctx.close();
    }

}

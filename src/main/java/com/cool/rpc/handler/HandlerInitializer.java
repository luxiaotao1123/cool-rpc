package com.cool.rpc.handler;


import com.cool.rpc.codec.CoolRpcDecoder;
import com.cool.rpc.codec.CoolRpcEncoder;
import com.cool.rpc.protocol.CoolRequest;
import com.cool.rpc.protocol.CoolResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.jboss.netty.channel.ChannelHandler;


/**
 * init channel pipeline and add all handlers
 * @author Vincent
 * @wechat  luxiaotao1123
 * @data  2018/8/15
 */
@ChannelHandler.Sharable
public class HandlerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * set some channel handlers on channel pipeline
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        socketChannel.pipeline()
                .addLast(new CoolRpcDecoder(CoolRequest.class))
                .addLast(new CoolRpcEncoder(CoolResponse.class))
                .addLast(new CoolServerHandler());
    }

}

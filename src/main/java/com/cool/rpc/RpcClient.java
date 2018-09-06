package com.cool.rpc;

import com.cool.rpc.handler.HandlerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RpcClient {

    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private Channel channel;
    private String serviceIP;
    private int port;

    {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
    }


    public RpcClient(String serviceIP, int port){
        this.serviceIP = serviceIP;
        this.port = port;
    }

    public RpcClient connect(){
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new HandlerInitializer(false))
                    .option(ChannelOption.TCP_NODELAY, true);

            channel = bootstrap.connect(serviceIP, port).sync().channel();
            return this;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void down(){
        if (group != null){
            group.shutdownGracefully();
        }
        if (channel != null){
            channel.close();
        }
    }

    public Channel getChannel(){
        return this.channel;
    }

}

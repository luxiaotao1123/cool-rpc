package com.cool.rpc.pool;

import com.cool.rpc.codec.CoolRpcDecoder;
import com.cool.rpc.codec.CoolRpcEncoder;
import com.cool.rpc.handler.CoolClientHandler;
import com.cool.rpc.protocol.CoolRequest;
import com.cool.rpc.protocol.CoolResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * cool rpc client bootstrap (netty)
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
public class CoolRpcClientPool {

    private static Logger log = LoggerFactory.getLogger(CoolRpcClientPool.class);

    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private Channel channel;
    private String serviceIP;
    private int port;

    {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
    }


    public CoolRpcClientPool(String serviceIP, int port){
        this.serviceIP = serviceIP;
        this.port = port;
    }


    public CoolRpcClientPool connect(){
        try {
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline()
                            .addLast(new CoolRpcDecoder(CoolResponse.class))
                            .addLast(new CoolRpcEncoder(CoolRequest.class));
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);

            channel = bootstrap.connect(serviceIP, port).sync().channel();
            channel.closeFuture().sync();
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

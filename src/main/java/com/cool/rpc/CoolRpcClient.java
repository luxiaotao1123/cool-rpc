package com.cool.rpc;

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


public class CoolRpcClient {

    private static Logger log = LoggerFactory.getLogger(CoolRpcClient.class);

    private CountDownLatch countDownLatch;
    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private CoolResponse response;
    private String serviceIP;
    private int port;

    {
        response = new CoolResponse();
        countDownLatch = new CountDownLatch(1);
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
    }


    public CoolRpcClient(String serviceIP, int port){
        this.serviceIP = serviceIP;
        this.port = port;
    }


    public CoolResponse send(CoolRequest request){
        try {
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline()
                            .addLast(new CoolRpcDecoder(CoolResponse.class))
                            .addLast(new CoolRpcEncoder(CoolRequest.class))
                            .addLast(new CoolClientHandler(countDownLatch, response));
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);

            Channel channel = bootstrap.connect(serviceIP, port).sync().channel();
            channel.writeAndFlush(request).sync();
            countDownLatch.await();
            channel.closeFuture().sync();

            return response;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            group.shutdownGracefully();
        }
    }


}

package com.cool.rpc;

import com.cool.rpc.annotation.CoolService;
import com.cool.rpc.center.ServiceCenter;
import com.cool.rpc.handler.HandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;


public class CoolRpcServer implements ApplicationContextAware {

    private static Logger log = LoggerFactory.getLogger(CoolRpcServer.class);
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private HandlerInitializer handlerInitializer;
    private ServiceCenter serviceRegistry;
    private String serviceIP;
    private int port;
    public static Map<String, Object> servicesMap ;

    {
        serviceIP = "localhost";
        port = 9523;
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        handlerInitializer = new HandlerInitializer();
        servicesMap = new HashMap<>(16);
    }

    public CoolRpcServer(ServiceCenter serviceRegistry){
        new CoolRpcServer(serviceRegistry, serviceIP, port);
    }

    public CoolRpcServer(ServiceCenter serviceRegistry, String serviceIP, int port){
        this.serviceRegistry = serviceRegistry;
        this.serviceIP = serviceIP;
        this.port = port;
    }

    /**
     * start and init tcp server if ioc contain is booting
     */
    @SuppressWarnings("unchecked")
    public void initServer() throws InterruptedException {

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(handlerInitializer);

        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // the most send bytes ( 256KB )
        bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 256);
        // the most receive bytes ( 2048KB )
        bootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 1024 * 2);

        channel = bootstrap.bind(serviceIP,port).sync().channel();

        if (servicesMap != null && servicesMap.size() > 0){
            for (String beanName: servicesMap.keySet()){
                serviceRegistry.register(beanName, serviceIP + ":" + String.valueOf(port));
                log.info("register service name = {}", beanName);
            }
        }
        log.info("TCP server started successfully, port：{}", port);

        channel.closeFuture().sync();
    }


    /**
     * close ioc contain and stop tcp server
     */
    public void stopServer(){

        if (channel != null && channel.isActive()) {
            channel.close();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }

        log.info("TCP server stopped successfully, port: {}", port);
    }

    /**
     *  scan Annotation of CoolService
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> beans = ctx.getBeansWithAnnotation(CoolService.class);
        if (beans != null && beans.size()>0){
            for (Object bean : beans.values()){
                String name = bean.getClass().getAnnotation(CoolService.class).value().getName();
                servicesMap.put(name, bean);
            }
        }
    }

}

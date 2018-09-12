package com.cool.rpc;

import com.cool.rpc.annotation.CoolService;
import com.cool.rpc.center.ServiceCenter;
import com.cool.rpc.center.ServiceCenterAdapter;
import com.cool.rpc.handler.HandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class RpcServer implements ApplicationContextAware, InitializingBean, DisposableBean {

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private HandlerInitializer handlerInitializer;
    private ServiceCenter serviceRegistry;
    private static String host;
    private static int port;
    public static Map<String, Object> servicesMap ;

    static {
        host = "localhost";
        port = 9523;
    }

    {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        handlerInitializer = new HandlerInitializer();
        servicesMap = new HashMap<>(16);
    }

    public RpcServer(ServiceCenter serviceRegistry){
        this(serviceRegistry, RpcServer.host, RpcServer.port);
    }

    public RpcServer(ServiceCenter serviceRegistry, String host, int port){
        this.serviceRegistry = serviceRegistry;
        RpcServer.host = host;
        RpcServer.port = port;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        initServer();
    }

    /**
     * start and init tcp server if ioc contain is booting
     */
    @SuppressWarnings("unchecked")
    private void initServer() throws InterruptedException {

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

        channel = bootstrap.bind(host,port).sync().channel();

        if (servicesMap != null && servicesMap.size() > 0){
            for (String beanName: servicesMap.keySet()){
                serviceRegistry.register(beanName, host + ":" + String.valueOf(port));
            }
        }

        channel.closeFuture().sync();
    }


    /**
     *  scan Annotation of CoolService
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> services = ctx.getBeansWithAnnotation(CoolService.class);
        if (services.size() > 0){
            for (Object bean : services.values()){
                String name = bean.getClass().getAnnotation(CoolService.class).value().getName();
                servicesMap.put(name, bean);
            }
        }

    }

    /**
     * close ioc contain and stop tcp server
     */
    @Override
    public void destroy() throws Exception {
        if (channel != null && channel.isActive()) {
            channel.close();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

}

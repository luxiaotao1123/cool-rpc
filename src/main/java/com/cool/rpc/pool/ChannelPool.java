package com.cool.rpc.pool;

import com.cool.rpc.CoolException;
import io.netty.channel.Channel;

import java.util.Random;


public class ChannelPool {

    private volatile static ChannelPool instance = null;
    private static final int DEFAULT_MAX_CHANNEL_COUNT = 5;
    private static String serverIp;
    private static int port;
    private Channel[] channels;
    private Object[] locks;
    private int maxChannelCount = 0;

    static {
        serverIp = "localhost";
        port = 9523;
    }

    private ChannelPool(int maxChannelCount){
        if (maxChannelCount <= 0){
            throw new CoolException("the channels pool can not be less then zero");
        }
        this.maxChannelCount = maxChannelCount;
        this.channels = new Channel[maxChannelCount];
        this.locks = new Object[maxChannelCount];
        for (int i = 0;i < maxChannelCount;i++){
            this.locks[i] = new Object();
        }
    }

    public static ChannelPool newChannelPool(){
        return newChannelPool(serverIp, port, DEFAULT_MAX_CHANNEL_COUNT);
    }

    public static ChannelPool newChannelPool(String serverIp, int port){
        return newChannelPool(serverIp, port, DEFAULT_MAX_CHANNEL_COUNT);
    }

    public static ChannelPool newChannelPool(String serverIp, int port, int maxChannelCount){
        ChannelPool.serverIp = serverIp;
        ChannelPool.port = port;
        if (instance == null){
            synchronized (ChannelPool.class){
                return new ChannelPool(maxChannelCount);
            }
        }
        return instance;
    }

    public Channel syncChannel(){
        int idx = new Random().nextInt(this.maxChannelCount == 0 ? DEFAULT_MAX_CHANNEL_COUNT : maxChannelCount);

        Channel channel = this.channels[idx];
        if (channel != null && channel.isActive()){
            return channel;
        }

        synchronized (this.locks[idx]){
            channel = channels[idx];
            if (channel != null && channel.isActive()){
                return channel;
            }

            CoolRpcClientPool connect = new CoolRpcClientPool(serverIp, port).connect();
            if (connect != null){
                channel = connect.getChannel();
            }
            channels[idx] = channel;
        }
        return channel;
    }



}

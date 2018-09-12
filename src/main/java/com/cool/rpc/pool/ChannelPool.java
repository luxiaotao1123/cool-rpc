package com.cool.rpc.pool;

import com.cool.rpc.CoolException;
import com.cool.rpc.RpcClient;
import io.netty.channel.Channel;

import java.util.Random;


public class ChannelPool {

    private volatile static ChannelPool instance = null;

    private static final int DEFAULT_MAX_CHANNEL_COUNT = 5;
    private static String host;
    private static int port;
    private static Channel[] channels;
    private Object[] locks;
    private int maxChannelCount = 0;

    static {
        host = "localhost";
        port = 9523;
    }

    private ChannelPool(int maxChannelCount){
        if (maxChannelCount <= 0){
            throw new CoolException("The channels pool can not be less then zero");
        }
        this.maxChannelCount = maxChannelCount;
        channels = new Channel[maxChannelCount];
        this.locks = new Object[maxChannelCount];
        for (int i = 0;i < maxChannelCount;i++){
            this.locks[i] = new Object();
        }
    }

    public static ChannelPool newChannelPool(){
        return newChannelPool(host, port, DEFAULT_MAX_CHANNEL_COUNT);
    }

    public static ChannelPool newChannelPool(String serverIp, int port){
        return newChannelPool(serverIp, port, DEFAULT_MAX_CHANNEL_COUNT);
    }

    public static ChannelPool newChannelPool(String host, int port, int maxChannelCount){
        ChannelPool.host = host;
        ChannelPool.port = port;
        if (instance == null){
            synchronized (ChannelPool.class){
                instance = new ChannelPool(maxChannelCount);
                return instance;
            }
        }
        return instance;
    }

    public Channel getChannel(){
        int idx = new Random().nextInt(this.maxChannelCount == 0 ? DEFAULT_MAX_CHANNEL_COUNT : maxChannelCount);

        Channel channel = channels[idx];
        if (channel != null && channel.isActive()){
            return channel;
        }

        synchronized (this.locks[idx]){
            channel = channels[idx];
            if (channel != null && channel.isActive()){
                return channel;
            }

            RpcClient connect = new RpcClient(host, port).connect();
            if (connect != null){
                channel = connect.getChannel();
            }
            channels[idx] = channel;
        }
        return channel;
    }

}

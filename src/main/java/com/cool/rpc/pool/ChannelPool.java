package com.cool.rpc.pool;

import io.netty.channel.Channel;

import java.util.Optional;
import java.util.Random;


public class ChannelPool {
//http://www.importnew.com/26701.html
    private Channel[] channels;
    private Object[] locks;
    private int maxChannelCount = 0;
    private static final int DEFAULT_MAX_CHANNEL_COUNT = 5;

    public ChannelPool(int maxChannelCount){
        this.channels = new Channel[maxChannelCount];
        this.locks = new Object[maxChannelCount];
        for (int i = 0;i < maxChannelCount;i++){
            this.locks[i] = new Object();
        }
    }

    public ChannelPool(){
        new ChannelPool(DEFAULT_MAX_CHANNEL_COUNT);
    }

    public Channel syncChannel(String serverIp, int port){
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

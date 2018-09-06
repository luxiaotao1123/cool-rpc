package com.cool.rpc.handler;


import com.cool.rpc.codec.CoolRpcDecoder;
import com.cool.rpc.codec.CoolRpcEncoder;
import com.cool.rpc.constant.ChannelAttrKey;
import com.cool.rpc.protocol.CoolRequest;
import com.cool.rpc.protocol.CoolResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Attribute;
import org.jboss.netty.channel.ChannelHandler;

import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class HandlerInitializer extends ChannelInitializer<Channel> {

    private boolean isServer;

    {
        isServer = true;
    }

    public HandlerInitializer(){
    }

    public HandlerInitializer(boolean isServer){
        this.isServer = isServer;
    }

    /**
     * Set some channel handlers on channel pipeline
     */
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new CoolRpcEncoder(this.isServer ? CoolResponse.class : CoolRequest.class));
        pipeline.addLast(new CoolRpcDecoder(this.isServer ? CoolRequest.class : CoolResponse.class));
        if (isServer){
            pipeline.addLast(new IdleStateHandler(720,0,0));
        }
        pipeline.addLast(this.isServer ? new ServerHandler() : new ClientHandler());

        if (!isServer){
            initAttrTrack(channel);
        }

    }

    /**
     *  Init channel attr track
     */
    private void initAttrTrack(Channel channel){
        Attribute<Map<Long, Object>> coolTrackAttr = channel.attr(ChannelAttrKey.DATA_MAP_ATTR);
        Map<Long, Object> trackMap = new HashMap<>(16);
        coolTrackAttr.setIfAbsent(trackMap);
    }


}

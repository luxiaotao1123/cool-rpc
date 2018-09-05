package com.cool.rpc.util;


import com.cool.rpc.constant.ChannelAttrKey;
import io.netty.channel.Channel;


public class ChannelTools {

    public static <T> void putCallback2DataMap(Channel channel, long seq, T callback) {
        channel.attr(ChannelAttrKey.DATA_MAP_ATTR).get().put(seq, callback);
    }

    public static <T> T removeCallback(Channel channel, int seq) {
        return (T) channel.attr(ChannelAttrKey.DATA_MAP_ATTR).get().remove(seq);
    }

}


package com.cool.rpc.util;


import com.cool.rpc.CallBack;
import com.cool.rpc.constant.ChannelAttrKey;
import io.netty.channel.Channel;

public class ChannelTools {

    public static void putData(Channel channel, Long seq, CallBack callback) {
        channel.attr(ChannelAttrKey.DATA_MAP_ATTR).get().put(seq, callback);
    }

    public static CallBack getRemove(Channel channel, Long requestId) {
        return (CallBack) channel.attr(ChannelAttrKey.DATA_MAP_ATTR).get().remove(requestId);
    }

}


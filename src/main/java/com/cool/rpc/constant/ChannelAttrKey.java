package com.cool.rpc.constant;


import io.netty.util.AttributeKey;

import java.util.Map;

public class ChannelAttrKey {

    private static final String CHANNEL_ATTR_KEY_COOL_TRACK = "channel.attr.cool.track";

    public static AttributeKey<Map<Long, Object>> DATA_MAP_ATTR = AttributeKey.newInstance(CHANNEL_ATTR_KEY_COOL_TRACK);

}

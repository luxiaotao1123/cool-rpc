package com.cool.rpc.codec;

import com.cool.rpc.protocol.CoolProtocol;
import com.cool.rpc.util.ProtostuffSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * cool rpc encoder
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
public final class CoolRpcEncoder extends MessageToByteEncoder<Object> implements CoolRpcCodec {

    private Class<? extends CoolProtocol> protocolClass;

    public CoolRpcEncoder(Class<? extends CoolProtocol> protocolClass){
        this.protocolClass = protocolClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (protocolClass.isInstance(msg)){
            byte[] data = ProtostuffSerialize.serialize(msg);
            out.writeBytes(data);
        }
    }

}

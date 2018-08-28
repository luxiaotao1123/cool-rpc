package com.cool.rpc.codec;

import com.cool.rpc.protocol.CoolProtocol;
import com.cool.rpc.util.ProtostuffSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


/**
 * cool rpc decoder
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
public final class CoolRpcDecoder extends ByteToMessageDecoder implements CoolRpcCodec {

    private Class<? extends CoolProtocol> protocolClass;

    public CoolRpcDecoder(Class<? extends CoolProtocol> protocolClass){
        this.protocolClass = protocolClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4){
            return;
        }

        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);

        CoolProtocol protocol = ProtostuffSerialize.deserialize(data, protocolClass);
        data = null;
        out.add(protocol);

    }
}

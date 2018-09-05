package com.cool.rpc.codec;

import com.cool.rpc.protocol.CoolProtocol;
import com.cool.rpc.util.ProtostuffSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public final class CoolRpcEncoder extends MessageToByteEncoder<Object> implements CoolRpcCodec {

    private Class<? extends CoolProtocol> protocolClass;

    public CoolRpcEncoder(Class<? extends CoolProtocol> protocolClass){
        this.protocolClass = protocolClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (protocolClass.isInstance(msg)){
            CoolProtocol protocol = (CoolProtocol) msg;
            byte[] data = ProtostuffSerialize.serialize(msg);
            out.writeByte(MESSAGE_SIGN);
            out.writeByte(MESSAGE_SIGN);
            out.writeLong(protocol.getRequestId());
            out.writeShort(data.length);
            out.writeBytes(data);
        }
    }

}

package com.cool.rpc;


import io.netty.buffer.ByteBuf;

public class CallBack {

    public volatile ByteBuf result;

    public void receiveMessage(ByteBuf receiveBuf) throws Exception {

        synchronized (this) {

            result = receiveBuf;
            this.notify();
        }
    }


}

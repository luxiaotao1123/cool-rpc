package com.cool.rpc;


import com.cool.rpc.protocol.CoolProtocol;

import java.util.concurrent.CountDownLatch;

public class CallBack {

    private volatile CoolProtocol result;
    private CountDownLatch countDownLatch;

    {
        countDownLatch = new CountDownLatch(1);
    }

    public void receiveMessage(CoolProtocol protocol) {
        synchronized (this) {
            this.result = protocol;
            countDownLatch.countDown();
        }
    }

    CoolProtocol getResult() {
        return this.result;
    }

    CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }
}

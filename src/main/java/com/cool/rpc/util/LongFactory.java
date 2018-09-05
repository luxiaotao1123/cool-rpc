package com.cool.rpc.util;

import java.util.concurrent.atomic.AtomicLong;

public class LongFactory {

    private LongFactory(){}

    private static class SingletonHolder {
        private static final AtomicLong INSTANCE = new AtomicLong();
    }

    public static final AtomicLong getInstance() {
        return SingletonHolder.INSTANCE;
    }
}

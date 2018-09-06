package com.cool.rpc;

public interface Proxy {

    <T> T getInstance(Class<T> cls);
}

package com.cool.rpc.center;


public interface ServiceCenter {

    String discover(String serviceName);

    void register(String serviceName, String serviceAddress);

    String getAddress();

    void setHost(String host);

    void setPort(int port);

    void setPassWord(String passWord);

}

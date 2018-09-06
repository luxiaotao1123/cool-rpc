package com.cool.rpc.center;


public interface ServiceCenter {

    int ZK_SESSION_TIMEOUT = 5000;
    int ZK_CONNECTION_TIMEOUT = 1000;
    String ZK_REGISTRY_PATH = "/cool";

    String discover(String serviceName);

    void register(String serviceName, String serviceAddress);

    String getAddress();

    void setHost(String host);

    void setPort(int port);

    void setPassWord(String passWord);

}

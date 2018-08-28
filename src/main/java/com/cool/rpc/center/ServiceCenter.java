package com.cool.rpc.center;


/**
 * service center
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
public interface ServiceCenter {

    String discover(String serviceName);

    void register(String serviceName, String serviceAddress);

    String getAddress();

    void setHost(String host);

    void setPort(int port);

    void setPassWord(String passWord);

}

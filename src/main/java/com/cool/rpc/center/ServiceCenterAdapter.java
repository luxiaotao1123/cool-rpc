package com.cool.rpc.center;

/**
 * service center adapter
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
public abstract class ServiceCenterAdapter implements ServiceCenter{

    String host;
    int port = 0;
    String passWord;

    ServiceCenterAdapter(){}

    ServiceCenterAdapter(String host){
        this.host = host;
    }

    ServiceCenterAdapter(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String discover(String serviceName) {
        return null;
    }

    @Override
    public void register(String serviceName, String serviceAddress) {}

    @Override
    public void setHost(String host){
        this.host = host;
    };

    @Override
    public void setPort(int port){
        this.port = port;
    };

    @Override
    public void setPassWord(String passWord){
        this.passWord = passWord;
    };

    @Override
    public String getAddress(){
        if ("".equals(host) || host == null || port == 0){
            throw new RuntimeException("the zookeeper host or port error");
        }
        return host+":"+String.valueOf(port);
    };
}

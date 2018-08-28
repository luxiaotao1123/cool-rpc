package com.cool.rpc.center;

import com.cool.rpc.constant.CoolConstant;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * service center registry (zookeeper)
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
public class ZooKeeperServiceRegistry extends ServiceCenterAdapter {

    private static final Logger log = LoggerFactory.getLogger(ZooKeeperServiceRegistry.class);

    private ZkClient zkClient;

    {
        this.port = 2181;
        zkClient = new ZkClient(getAddress(), CoolConstant.ZK_SESSION_TIMEOUT, CoolConstant.ZK_CONNECTION_TIMEOUT);
        log.info("connect zookeeper");
    }

    public ZooKeeperServiceRegistry(String zkHost) {
        super(zkHost);
    }

    public ZooKeeperServiceRegistry(String zkHost, int zkPort) {
        super(zkHost, zkPort);
    }


    @Override
    public void register(String serviceName, String serviceAddress) {
        // create cool node permanent
        String registryPath = CoolConstant.ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            log.info("create registry node: {}", registryPath);
        }
        // create service node permanent
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            log.info("create service node: {}", servicePath);
        }
        // create service address node temp
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
        log.info("create address node: {}", addressNode);
    }

}
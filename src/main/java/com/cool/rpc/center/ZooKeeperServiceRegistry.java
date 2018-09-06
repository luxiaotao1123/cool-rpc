package com.cool.rpc.center;

import org.I0Itec.zkclient.ZkClient;

public class ZooKeeperServiceRegistry extends ServiceCenterAdapter {

    private ZkClient zkClient;

    {
        this.port = 2181;
        zkClient = new ZkClient(getAddress(), ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT);
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
        String registryPath = ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
        }
        // create service node permanent
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
        }
        // create service address node temp
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
    }

}
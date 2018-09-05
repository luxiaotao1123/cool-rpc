package com.cool.rpc.center;

import com.cool.rpc.constant.CoolConstant;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;



public class ZooKeeperServiceDiscovery extends ServiceCenterAdapter {

    private static final Logger log = LoggerFactory.getLogger(ZooKeeperServiceDiscovery.class);

    {
        super.port = 2181;
    }

    public ZooKeeperServiceDiscovery(){};

    public ZooKeeperServiceDiscovery(String zkHost){
        super(zkHost);
    }

    public ZooKeeperServiceDiscovery(String zkHost, int zkPort){
        super(zkHost, zkPort);
    }

    @Override
    public String discover(String name) {

        ZkClient zkClient = new ZkClient(getAddress(), CoolConstant.ZK_SESSION_TIMEOUT, CoolConstant.ZK_CONNECTION_TIMEOUT);
        log.debug("connect zookeeper");
        try {
            String servicePath = CoolConstant.ZK_REGISTRY_PATH + "/" + name;
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if (addressList.size() == 0) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }
            String address;
            int size = addressList.size();
            if (size == 1) {
                address = addressList.get(0);
                log.debug("get only address node: {}", address);
            } else {
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                log.debug("get random address node: {}", address);
            }
            String addressPath = servicePath + "/" + address;
            return zkClient.readData(addressPath);
        } finally {
            zkClient.close();
        }
    }



}
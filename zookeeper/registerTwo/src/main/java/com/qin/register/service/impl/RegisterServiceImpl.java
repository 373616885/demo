package com.qin.register.service.impl;

import com.qin.register.service.RegisterService;
import org.apache.zookeeper.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;


@Service("registerService")
public class RegisterServiceImpl implements RegisterService {

    private final String CLIENT_PORT = "2181";
    private final String ZK_IP = "47.100.185.77";//"localhost";
    private final Integer CONNECTION_TIMEOUT = 1000;

    private final static String REGISTRY_PATH = "/registry";

    private final static String SERVICE = "/service";

    private final static String CLIENT = "/client";

    private ZooKeeper zk = null;
    private CountDownLatch latch = new CountDownLatch(1);

    @PostConstruct
    public void init() {
        try {
            zk = new ZooKeeper(ZK_IP + ":" + CLIENT_PORT, CONNECTION_TIMEOUT, event -> {
                System.out.println("收到事件通知：" + event.getState());
                if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                    if (Watcher.Event.EventType.None == event.getType() && null == event.getPath()) {
                        System.out.println("Service Zookeeper session established");
                        latch.countDown();
                    }
                }

                if (Watcher.Event.EventType.NodeCreated == event.getType()) {
                    System.out.println("Service success create znode" + event.getPath());
                } else if (Watcher.Event.EventType.NodeDataChanged == event.getType()) {
                    System.out.println("Service success change znode: " + event.getPath());
                } else if (Watcher.Event.EventType.NodeDeleted == event.getType()) {
                    System.out.println("Service success delete znode");
                } else if (Watcher.Event.EventType.NodeChildrenChanged == event.getType()) {
                    System.out.println("Service NodeChildrenChanged");
                }

            });
            //
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean register(String hostName) {

        try {
            // 根节点
            if (zk.exists(REGISTRY_PATH, false) == null) {
                zk.create(REGISTRY_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            // 服务节点 (这个节点是配置文件里配置)
            if (zk.exists(REGISTRY_PATH + SERVICE, false) == null) {
                zk.create(REGISTRY_PATH + SERVICE, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            // 临时子节点
            zk.create(REGISTRY_PATH + SERVICE + CLIENT, hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);


            return true;

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return false;
    }
}

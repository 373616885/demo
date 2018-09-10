package com.qin.discovery.service.impl;

import com.google.common.collect.Lists;
import com.qin.discovery.service.DiscoveryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service("discoveryService")
public class DiscoveryServiceImpl implements DiscoveryService {

    private List<String> load = Lists.newArrayList();

    private final String CLIENT_PORT = "2181";
    private final String ZK_IP = "localhost";
    private final Integer CONNECTION_TIMEOUT = 1000;

    private final static String REGISTRY_PATH = "/registry";

    private final static String SERVICE = "/service";

    private ZooKeeper zk = null;
    private CountDownLatch latch = new CountDownLatch(1);

    @PostConstruct
    public void init() {
        try {
            zk = new ZooKeeper(ZK_IP + ":" + CLIENT_PORT + REGISTRY_PATH, CONNECTION_TIMEOUT, event -> {
                System.out.println("收到事件通知：" + event.getState());
                if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                    if (Watcher.Event.EventType.None == event.getType() && null == event.getPath()) {
                        System.out.println("client Zookeeper session established");
                        updateLoad();
                        latch.countDown();
                    }
                }
                // 注册子节点发生变化修改load
                if (Watcher.Event.EventType.NodeChildrenChanged == event.getType()
                        && StringUtils.equals(REGISTRY_PATH + SERVICE, event.getPath())) {

                    updateLoad();

                } else if (Watcher.Event.EventType.NodeCreated == event.getType()) {
                    System.out.println("client success create znode" + event.getPath());
                } else if (Watcher.Event.EventType.NodeDataChanged == event.getType()) {
                    System.out.println("client success change znode: " + event.getPath());
                } else if (Watcher.Event.EventType.NodeDeleted == event.getType()) {
                    System.out.println("client success delete znode");
                } else if (Watcher.Event.EventType.NodeChildrenChanged == event.getType()) {
                    System.out.println("client NodeChildrenChanged");
                }


            });
            //
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateLoad() {

        List<String> newLoad = Lists.newArrayList();

        try {
            List<String> children = zk.getChildren( SERVICE, event -> {
                if (Watcher.Event.EventType.NodeChildrenChanged == event.getType()
                        && StringUtils.equals(SERVICE, event.getPath())) {
                    System.out.println("zk.getChildre 的监听器发生变化");
                    updateLoad();
                }
            });
            for (String subNode : children) {
                byte[] data = zk.getData(SERVICE + "/" + subNode, null, null);
                newLoad.add(new String(data, "UTF-8"));
            }
            load = newLoad;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private String loadBalance() {

        if (load.isEmpty()) {
            return null;
        }

        if (load.size() == 1) {
            return load.get(0);
        }

        return load.get((int) (Math.random() * load.size()));

    }

    @Override
    public String discovery() {
        return loadBalance();
    }

}

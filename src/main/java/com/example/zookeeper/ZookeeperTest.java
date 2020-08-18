package com.example.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZookeeperTest {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String connStr = "127.0.0.1:2181";
        CountDownLatch countDown = new CountDownLatch(1);
        Watcher watcher = event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("eventType:" + event.getType());
                if (event.getType() == Watcher.Event.EventType.None) {
                    countDown.countDown();
                } else if (event.getType() == Watcher.Event.EventType.NodeCreated) {
                    System.out.println("listen:节点创建");
                } else if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    System.out.println("listen:子节点修改");
                }
            }
        };
        ZooKeeper zookeeper = new ZooKeeper(connStr, 50000, watcher);
        countDown.await();

        String zNode = "/Zuo6";
        //注册监听,每次都要重新注册，否则监听不到
        zookeeper.exists(zNode, watcher);

        // 创建节点
        String result = zookeeper.create(zNode, "一生一世".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        log.error(result);
        System.out.println(result);

        Thread.sleep(10);

        // 获取节点
        //按字节存取
        byte[] bs = zookeeper.getData(zNode, true, null);
        result = new String(bs);
        System.out.println("创建节点后的数据是:" + result);

        // 修改节点
        zookeeper.setData(zNode, "I love you".getBytes(), -1);

        Thread.sleep(10);

        bs = zookeeper.getData(zNode, true, null);
        result = new String(bs);
        System.out.println("修改节点后的数据是:" + result);

        // 删除节点
        zookeeper.delete(zNode, -1);
        System.out.println("节点删除成功");
    }
}

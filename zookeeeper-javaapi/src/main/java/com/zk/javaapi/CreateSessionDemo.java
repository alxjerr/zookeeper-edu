package com.zk.javaapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CreateSessionDemo {

    private final static String CONNECTSTRING="192.168.9.91:2181,192.168.9.92:2181,192.168.9.79:2181";

    private static CountDownLatch countDownLatch=new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper(CONNECTSTRING, 500, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                //如果当前的连接状态是连接成功的，那么通过计数器去控制
                if(event.getState() == Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                    System.out.println(event.getState());
                }
            }
        });
        countDownLatch.await();
        System.out.println(zooKeeper.getState());
    }

}

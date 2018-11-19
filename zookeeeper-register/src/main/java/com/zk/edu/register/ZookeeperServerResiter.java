package com.zk.edu.register;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 注册中心 对外提供注册服务
 */
public class ZookeeperServerResiter {

    private ZooKeeper zk;

    public static final String root="/jerry";
    private static final String host = "192.168.9.91:2181,192.168.9.92:2181,192.168.9.79:2181";

    public ZooKeeper getConnection(Watcher watch) throws IOException {
        zk = new ZooKeeper(host,500,watch);
        return zk;
    }

}

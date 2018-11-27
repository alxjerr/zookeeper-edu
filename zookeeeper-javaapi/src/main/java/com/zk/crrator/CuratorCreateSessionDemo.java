package com.zk.crrator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.sql.Connection;

public class CuratorCreateSessionDemo {

    private final  static String CONNECTSTRING="192.168.9.91:2181,192.168.9.92:2181,192.168.9.79:2181";

    public static void main(String[] args) {
        // normal
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECTSTRING, 5000, 5000,
                new ExponentialBackoffRetry(1000, 3));
        curatorFramework.start();

        // fluent
        CuratorFramework builder = CuratorFrameworkFactory.builder()
                .connectString(CONNECTSTRING)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("/curator").build();
        builder.start();
    }


}

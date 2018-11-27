package com.zk.crrator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorClientUtils {

    private static CuratorFramework curatorFramework;
    private final  static String CONNECTSTRING="192.168.9.91:2181,192.168.9.92:2181,192.168.9.79:2181";


    public static CuratorFramework getInstance(){
        curatorFramework = CuratorFrameworkFactory
                .newClient(CONNECTSTRING,5000,5000,
                        new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();
        return curatorFramework;
    }

}

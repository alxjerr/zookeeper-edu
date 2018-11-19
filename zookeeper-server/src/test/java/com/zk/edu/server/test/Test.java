package com.zk.edu.server.test;


import com.zk.edu.server.watcher.ZkWatcherCrud;
import org.apache.zookeeper.KeeperException;

public class Test {

    public static void main(String[] args) throws KeeperException, InterruptedException {

        ZkWatcherCrud zookeeperCrud=new ZkWatcherCrud();
        ZkWatcherCrud zookeeperCrud2=new ZkWatcherCrud();

        //  zookeeperCrud.delete("/wukong");
//        zookeeperCrud.createEphemeral("/jerry","abc");
        zookeeperCrud.createPersistent("/jerry","abc");


        System.out.println(zookeeperCrud.getData("/jerry",true));;
        Thread.sleep(Long.MAX_VALUE);
    }

}

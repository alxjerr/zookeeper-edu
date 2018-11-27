package com.zk.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ApiOperatorDemo implements Watcher {

    private final  String CONNECTSTRING="192.168.9.91:2181,192.168.9.92:2181,192.168.9.79:2181";
    private static CountDownLatch countDownLatch=new CountDownLatch(1);
    private  ZooKeeper zookeeper;
    private  Stat stat=new Stat();

    public ApiOperatorDemo() throws Exception {
       zookeeper = new ZooKeeper(CONNECTSTRING,5000,this);
       countDownLatch.await();
       System.out.println(zookeeper.getState());
    }


    @Test
    public  void createNode() throws Exception {
        //创建节点
        String result = zookeeper.create("/node", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zookeeper.getData("/node",this,stat);
        System.out.println("创建成功：" + result);
    }


    @Override
    public void process(WatchedEvent event) {
        if(event.getState()==Event.KeeperState.SyncConnected){
            if(Event.EventType.None==event.getType() && null == event.getPath()){
                countDownLatch.countDown();
                System.out.println(event.getState() + "-->" + event.getType());
            }else if(event.getType() == Event.EventType.NodeDataChanged){
                try {
                    System.out.println("数据变更触发路径："
                                        +event.getPath()
                                        + "->改变后的值："
                                        + zookeeper.getData(event.getPath(),true,stat));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(event.getType() == Event.EventType.NodeChildrenChanged){//子节点的数据变化会触发
                try {
                    System.out.println("子节点数据变更路径："
                                        + event.getPath()
                                        + "子节点的值："
                                        + zookeeper.getData(event.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(event.getType() ==Event.EventType.NodeCreated){//创建子节点的时候会触发
                try {
                    System.out.println("节点创建路径：" + event.getPath() + "->节点的值：" +
                                    zookeeper.getData(event.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(event.getType() == Event.EventType.NodeDeleted){ //子节点删除会触发
                System.out.println("节点删除路径：" + event.getPath());
            }
            System.out.println(event.getType());
        }
    }

    public static void main(String[] args) throws Exception {
        ApiOperatorDemo apiOperatorDemo = new ApiOperatorDemo();
//        apiOperatorDemo.createNode();
    }
}

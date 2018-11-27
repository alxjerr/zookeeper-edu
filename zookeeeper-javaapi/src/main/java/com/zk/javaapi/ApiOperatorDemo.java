package com.zk.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ApiOperatorDemo implements Watcher {

    private final  static String CONNECTSTRING="192.168.9.91:2181,192.168.9.92:2181,192.168.9.79:2181";
    private static CountDownLatch countDownLatch=new CountDownLatch(1);
    private static  ZooKeeper zookeeper;
    private static Stat stat=new Stat();

    /*public ApiOperatorDemo() throws Exception {
       zookeeper = new ZooKeeper(CONNECTSTRING,5000,this);
       countDownLatch.await();
       System.out.println(zookeeper.getState());
    }*/


    @Test
    public  void createNode() throws Exception {
        //创建节点
        String result = zookeeper.create("/node", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zookeeper.getData("/node",this,stat);
        System.out.println("创建成功：" + result);
    }

    @Test
    public void updateData() throws Exception {
        zookeeper.setData("/node","mic123".getBytes(),-1);
        Thread.sleep(2000);
        zookeeper.setData("/node","mic245".getBytes(),-1);
        Thread.sleep(2000);
    }


    public static void main(String[] args) throws Exception {
        zookeeper=new ZooKeeper(CONNECTSTRING, 5000, new ApiOperatorDemo());
        countDownLatch.await();


        //创建节点
        String result=zookeeper.create("/node1","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zookeeper.getData("/node1",new ApiOperatorDemo(),stat); //增加一个
        System.out.println("创建成功："+result);

        //修改数据
        zookeeper.setData("/node1","mic123".getBytes(),-1);
        Thread.sleep(2000);
        //修改数据
        zookeeper.setData("/node1","mic234".getBytes(),-1);
        Thread.sleep(2000);

        //删除节点
//        zookeeper.delete("/node",-1);
//        Thread.sleep(2000);
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

}

package com.zk.edu.server.watcher;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;


public class ZkWatcherCrud implements Watcher {

    private String connectString="192.168.9.91:2181,192.168.9.92:2181,192.168.9.79:2181";

    private ZooKeeper zookeeper;

    public ZkWatcherCrud() {
        try {
            zookeeper=new ZooKeeper(connectString,5000,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 创建持久节点
     * @param path
     * @param data
     * @return
     */
    public String createPersistent(String path,String data){
        try {
            return  zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /***
     * 创建临时节点
     * @param path
     * @param data
     * @return
     */
    public String createEphemeral(String path,String data){
        try {
            return  zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /** 获取信息  */
    public String getData(String path,boolean watcher) throws KeeperException, InterruptedException {
        byte[] data = zookeeper.getData(path, watcher, null);
        data = data == null ? "null".getBytes() : data;
        return new String(data);
    }

    /** 修改信息 */
    public Stat setData(String path,String data) throws KeeperException, InterruptedException {
        return zookeeper.setData(path,data.getBytes(),-1);
    }

    /** 判断是否存在 */
    public Stat exists(String path,boolean watcher) throws KeeperException, InterruptedException {
        return zookeeper.exists(path,watcher);
    }


    /***
     * 删除
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void delete(String path) throws KeeperException, InterruptedException {
        zookeeper.delete(path,-1);
    }
    /***
     * 删除
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void deleteRecursive(String path) throws KeeperException, InterruptedException {
        ZKUtil.deleteRecursive(zookeeper, path);
    }

    public void close() throws InterruptedException {
        zookeeper.close();
    }

    public void process(WatchedEvent event) {
        // 获取状态
        Event.KeeperState keeperState = event.getState();
        // 事件类型
        Event.EventType eventType = event.getType();
        // 受影响的path
        String path = event.getPath();

        try {
            if(null!=this.exists("/jerry",true)) {
                System.out.println("内容:"+ this.getData("/jerry", true));
            }
            System.out.println("连接状态:"+keeperState+",事件类型："+eventType+",受影响的path:"+path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--------------------");
    }
}

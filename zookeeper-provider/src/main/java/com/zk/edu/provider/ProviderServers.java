package com.zk.edu.provider;

import com.alibaba.fastjson.JSON;
import com.zk.edu.register.ServerStatus;
import com.zk.edu.register.StatDto;
import com.zk.edu.register.ZookeeperServerResiter;
import org.apache.zookeeper.*;

import java.io.IOException;

public class ProviderServers implements Watcher {

    ZookeeperServerResiter bitZookeeperServer=new ZookeeperServerResiter();

    /**
     * 注册
     * @param serverName
     */
    public void register(String serverName) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zooKeeper = bitZookeeperServer.getConnection(this);

        String node = zooKeeper.create(ZookeeperServerResiter.root+"/server",
                        serverName.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println("创建成功过"+serverName+"，节点："+node);

    }



    public void process(WatchedEvent event) {

    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        ProviderServers server=new ProviderServers();
        StatDto stat=new StatDto();
        int i=0;

        stat.setIp(args[i++]);
        stat.setPort(args[i++]);
        stat.setName(args[i++]);
        stat.setNum(0);
        stat.setStatus(ServerStatus.wait);
        server.register(JSON.toJSONString(stat));

        Thread.sleep(Long.MAX_VALUE);
    }

}

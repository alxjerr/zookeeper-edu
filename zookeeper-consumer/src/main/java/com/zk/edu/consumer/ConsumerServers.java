package com.zk.edu.consumer;

import com.alibaba.fastjson.JSON;
import com.zk.edu.register.ServerStatus;
import com.zk.edu.register.StatDto;
import com.zk.edu.register.ZookeeperServerResiter;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.Watcher.Event.EventType;
import java.security.SecureRandom;
import java.util.List;


public class ConsumerServers implements Watcher {

    Logger logger = LoggerFactory.getLogger(ConsumerServers.class);

    ZookeeperServerResiter bitZook = new ZookeeperServerResiter();

    private ZooKeeper zook;
    private String clientName;

    public StatDto userServer(List<String> data, int dom, int i) throws Exception {
        if(data.size() == i){
            throw new Exception("没有可用的服务");
        }

        String node = data.get(dom);

        byte[] bytes = zook.getData(ZookeeperServerResiter.root + "/" + node,
                true,null);

        String datas = new String(bytes);

        StatDto dto = JSON.parseObject(datas,StatDto.class);

        if(ServerStatus.stop.equals(dto.getStatus())){
            i++;
            userServer(data,dom,i);
        }

        dto.setNode(node);
        return dto;

    }

    /**
     * 客户端订阅
     * @param clientName
     * @throws Exception
     */
    public void subscribe(String clientName) throws Exception {
        this.clientName = clientName;
        zook = bitZook.getConnection(this);
        List<String> data = zook.getChildren(ZookeeperServerResiter.root,true);
        if(data.isEmpty()){
            throw new Exception("没有可用的服务");
        }

        //随机
        int dom = new SecureRandom().nextInt(data.size());
        StatDto dto = userServer(data,dom,0);
        dto.setNum(dto.getNum() + 1);
        dto.setName(dto.getName());
        dto.setClient(dto.getClient() == null ? clientName : dto.getClient() + "," + clientName);
        dto.setStatus(ServerStatus.run);
        zook.setData(ZookeeperServerResiter.root + "/" + dto.getNode(),
                        JSON.toJSONString(dto).getBytes(),-1);
    }

    public void call() throws InterruptedException {
        System.out.println("客户端开启,建立netty连接");
    }

    public void process(WatchedEvent event) {

        if(event.getType() == EventType.NodeChildrenChanged){
            System.out.println("服务器发生改变，重新订阅");
        }

    }

    public static void main(String[] args) throws Exception {
        ConsumerServers client = new ConsumerServers();
        client.subscribe(args[0]);

        client.call();

        Thread.sleep(Long.MAX_VALUE);

    }

}

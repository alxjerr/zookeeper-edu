package com.zk.edu.client.znode;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class ZkClientCrud<T> {

    ZkClient zkClient;
    private String connectString="192.168.9.91:2181,192.168.9.92:2181,192.168.9.79:2181";
    public ZkClientCrud() {
        this.zkClient = new ZkClient(connectString,5000,5000,new SerializableSerializer());
    }




}

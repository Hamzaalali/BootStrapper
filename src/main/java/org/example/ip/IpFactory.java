package org.example.ip;

import org.example.auth.User;
import org.example.auth.UsersFactory;

import java.util.ArrayList;
import java.util.List;

public class IpFactory {
    private static IpFactory instance;
    private static int nodesNumber=1;
    private IpFactory(){

    }
    public List<String> getIps(){
        List<String >ipList=new ArrayList<>();
        ipList.add("127.0.0.1");
        return ipList;
    }
    public static IpFactory getInstance() {
        if (instance == null) {
            instance = new IpFactory();
        }
        return instance;
    }
}

package org.example.load.balancer;

import org.example.auth.User;
import org.example.auth.UsersFactory;
import org.example.ip.IpFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBalancer {
    Map<String, List<User>> nodeUsers;
    private static LoadBalancer instance;

    private LoadBalancer(){
        nodeUsers=new HashMap<>();
    }
    public Map<String,List<User>>getNodeUsers(){
        List<String> ipList= IpFactory.getInstance().getIps();
        List<User> userList=UsersFactory.getInstance().getUsers();
        int usersForEachNode=userList.size()/ipList.size();
        int ip=0;
        List<User> ipUsers=new ArrayList<>();
        for(int i=0;i<userList.size();i++){
            ipUsers.add(userList.get(i));
            if(ipUsers.size()==usersForEachNode){
                nodeUsers.put(ipList.get(ip),ipUsers);
                ipUsers=new ArrayList<>();
                ip++;
            }
        }
        return nodeUsers;
    }
    public static LoadBalancer getInstance() {
        if (instance == null) {
            instance = new LoadBalancer();
        }
        return instance;
    }
}

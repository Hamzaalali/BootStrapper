package org.example.load.balancer;
import org.example.auth.User;
import org.example.auth.UsersManager;
import org.example.port.PortsManager;
import org.example.udp.UdpManager;
import org.example.udp.UdpRoutineTypes;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBalancer {
    Map<Integer, List<User>> nodeUsers;
    private static LoadBalancer instance;
    private int portPointer;
    List<Integer> ports;
    private LoadBalancer(){
        nodeUsers=new HashMap<>();
        ports=PortsManager.getInstance().getPorts();
        balance();
        portPointer=-1;
    }
    public Map<Integer,List<User>>balance(){
        for(int i=0;i<ports.size();i++){
            nodeUsers.put(ports.get(i),new ArrayList<>());
        }
        List<User> userList= UsersManager.getInstance().getUsers();
        for(int i=0;i<userList.size();i++){
            nodeUsers.get(ports.get(nextPort())).add(userList.get(i));
        }
        return nodeUsers;
    }
    public void balanceUser(User user) throws IOException {
        int portIndex=nextPort();
        int port=ports.get(portIndex)-1000;
        nodeUsers.get(ports.get(portIndex)).add(user);
        JSONObject routine=user.toJson();
        routine.put("routineType", UdpRoutineTypes.ADD_USER.toString());
        UdpManager.getInstance().sendUdp(port,routine.toJSONString());
    }
    public Integer nextPort(){
        portPointer++;
        portPointer%=ports.size();
        return portPointer;
    }
    public List<User> getPortUsers(int port){
        return nodeUsers.get(port);
    }
    public static LoadBalancer getInstance() {
        if (instance == null) {
            instance = new LoadBalancer();
        }
        return instance;
    }
}

package org.example.load.balancer;
import org.example.auth.User;
import org.example.auth.UsersManager;
import org.example.node.Node;
import org.example.node.NodesManager;
import org.example.udp.CommandTypes;
import org.example.udp.UdpManager;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBalancer {
    Map<Node, List<User>> nodeUsers;
    private static LoadBalancer instance;
    private int nodePointer;
    List<Node> nodes;

    private LoadBalancer(){
        nodeUsers=new HashMap<>();
        nodes= NodesManager.getInstance().getNodes();
        balance();
        nodePointer =-1;
    }
    public Map<Node,List<User>>balance(){
        for(int i=0;i<nodes.size();i++){
            nodeUsers.put(nodes.get(i),new ArrayList<>());
        }
        List<User> userList= UsersManager.getInstance().getUsers();
        for(int i=0;i<userList.size();i++){
            nodeUsers.get(nodes.get(nextNodeNumber())).add(userList.get(i));
        }
        return nodeUsers;
    }
    public int balanceUser(User user) throws IOException {
        int nodeNumber= nextNodeNumber();
        Node node=nodes.get(nodeNumber);
        nodeUsers.get(nodes.get(nodeNumber)).add(user);
        JSONObject routine=user.toJson();
        routine.put("commandType", CommandTypes.ADD_USER.toString());
        UdpManager.getInstance().sendUdp(node.getUdpPort(), routine.toJSONString());
        return node.getTcpPort();
    }
    public Integer nextNodeNumber(){
        nodePointer++;
        nodePointer %=nodes.size();
        return nodePointer;
    }
    public List<User> getNodeUsers(int nodeNumber){
        return nodeUsers.get(nodes.get(nodeNumber-1));
    }
    public static LoadBalancer getInstance() {
        if (instance == null) {
            instance = new LoadBalancer();
        }
        return instance;
    }
}

package org.example.udp.routine;
import org.example.auth.User;
import org.example.load.balancer.LoadBalancer;
import org.example.node.Node;
import org.example.node.NodesManager;
import org.example.udp.CommandTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
public class InitlializeRoutine extends UdpRoutine{

    @Override
    public void execute(DatagramPacket packet, DatagramSocket socket,int nodeNumber) {
        List<User> userList= LoadBalancer.getInstance().getNodeUsers(nodeNumber);
        JSONObject initializeObject=new JSONObject();
        JSONArray users=new JSONArray();
        for(User user:userList){
            JSONObject userJson=new JSONObject();
            userJson.put("username",user.getUsername());
            userJson.put("password",user.getPassword());
            users.add(userJson);
        }
        Node thisNode=NodesManager.getInstance().getNode(nodeNumber);
        initializeObject.put("users",users);
        initializeObject.put("tcpPort",thisNode.getTcpPort());
        initializeObject.put("loadBalanceTimeWindow",5);
        initializeObject.put("loadBalanceMaxRequests",5);
        List<Node> nodesList= NodesManager.getInstance().getNodes();
        JSONArray nodes=new JSONArray();
        for(Node node:nodesList){
            nodes.add(node.toJson());
        }
        initializeObject.put("nodes",nodes);
        initializeObject.put("commandType", CommandTypes.INITIALIZE.toString());
        packet = new DatagramPacket(initializeObject.toString().getBytes(), initializeObject.toString().getBytes().length, packet.getAddress(), thisNode.getUdpPort());
        try{
           socket.send(packet);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

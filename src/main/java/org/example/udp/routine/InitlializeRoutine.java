package org.example.udp.routine;

import org.example.auth.User;
import org.example.load.balancer.LoadBalancer;
import org.example.port.PortsManager;
import org.example.udp.UdpManager;
import org.example.udp.UdpRoutineTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.List;

public class InitlializeRoutine extends UdpRoutine{

    @Override
    public void execute(DatagramPacket packet, DatagramSocket socket,int port) {
        List<User> userList= LoadBalancer.getInstance().getPortUsers(port);
        JSONObject initializeObject=new JSONObject();
        JSONArray users=new JSONArray();
        for(User user:userList){
            JSONObject userJson=new JSONObject();
            userJson.put("username",user.getUsername());
            userJson.put("password",user.getPassword());
            users.add(userJson);
        }
        initializeObject.put("users",users);
        initializeObject.put("tcpPort",port-2000);
        initializeObject.put("loadBalanceTimeWindow",5);
        initializeObject.put("loadBalanceMaxRequests",5);
        List<Integer> portsList= PortsManager.getInstance().getPorts();
        JSONArray ports=new JSONArray();
        for(int i:portsList){
            JSONObject nodePort=new JSONObject();
            nodePort.put("port",i-2000);
            ports.add(nodePort);
        }
        initializeObject.put("ports",ports);
        initializeObject.put("routineType", UdpRoutineTypes.INIT.toString());
        System.out.println(port);
        packet = new DatagramPacket(initializeObject.toString().getBytes(), initializeObject.toString().getBytes().length, packet.getAddress(), port-1000);
        try{
           socket.send(packet);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

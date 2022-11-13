package org.example.udp.routine;

import org.example.auth.User;
import org.example.load.balancer.LoadBalancer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.DatagramPacket;
import java.util.List;

public class InitlializeRoutine extends UdpRoutine{

    @Override
    public DatagramPacket execute(DatagramPacket packet,int port) {
        List<User> userList= LoadBalancer.getInstance().getPortUsers(port);
        JSONArray jsonArray=new JSONArray();
        for(User user:userList){
            JSONObject userJson=new JSONObject();
            userJson.put("username",user.getUsername());
            userJson.put("password",user.getPassword());
            userJson.put("port", packet.getPort());
            jsonArray.add(userJson);
        }
        packet = new DatagramPacket(jsonArray.toString().getBytes(), jsonArray.toString().getBytes().length, packet.getAddress(), packet.getPort());
        return packet;
    }
}

package org.example.node;

import org.example.auth.User;
import org.example.load.balancer.LoadBalancer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NodeToNodeReceiver implements Runnable {
    private DatagramSocket socket;
    private boolean running;
    InetAddress address;
    private byte[] buf = new byte[256];
    private int port;
    public NodeToNodeReceiver(int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket(port);
        address = InetAddress.getByName("127.0.0.1");
        this.port=port;
    }
    @Override
    public void run() {
        running = true;
        try{
            while (running) {
                Thread.sleep(5000);
                DatagramPacket packet  = new DatagramPacket("0".getBytes(), "0".getBytes().length, address, port);
//                socket.send(packet);
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received
                        = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received+" , from :"+packet.getAddress()+":"+packet.getPort());
//                if(Objects.equals(received, "i'm up")){
                    List<User> userList=LoadBalancer.getInstance().getNodeUsers().get(packet.getAddress().getHostAddress());
                    JSONArray jsonArray=new JSONArray();
                    for(User user:userList){
                        JSONObject userJson=new JSONObject();
                        userJson.put("username",user.getUsername());
                        userJson.put("password",user.getPassword());
                        userJson.put("port",port);
                        jsonArray.add(userJson);
                    }
                    packet = new DatagramPacket(jsonArray.toString().getBytes(), jsonArray.toString().getBytes().length, packet.getAddress(), packet.getPort());
                    socket.send(packet);
                }
//            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

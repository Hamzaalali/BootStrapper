package org.example.node;

import org.example.auth.User;
import org.example.load.balancer.LoadBalancer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class NodeConnectionManager {
    private static NodeConnectionManager instance;
    private NodeConnectionManager(){

    }
    public void listen(int port)  {
        NodeConnection nodeConnection=new NodeConnection(port);
        new Thread(nodeConnection).start();
    }
    public static NodeConnectionManager getInstance() {
        if (instance == null) {
            instance = new NodeConnectionManager();
        }
        return instance;
    }
    private class NodeConnection implements Runnable{
        private Socket socket;
        private int port;
        public NodeConnection(int port)  {
            this.port=port;
        }
        @Override
        public void run() {
            try{
                while(true){
                    System.out.println("test"+port);
                    ServerSocket serverSocket=new ServerSocket(port);
                    socket=serverSocket.accept();
                    System.out.println(socket.getInetAddress().getHostAddress());
                    System.out.println(socket.getPort());
                    List<User> userList=LoadBalancer.getInstance().getNodeUsers().get(socket.getInetAddress().getHostAddress());
                    JSONArray jsonArray=new JSONArray();
                    for(User user:userList){
                        JSONObject userJson=new JSONObject();
                        userJson.put("username",user.getUsername());
                        userJson.put("password",user.getPassword());
                        jsonArray.add(userJson);
                    }
                    ServerClientCommunicator.sendJsonArray(socket,jsonArray);
                }
            }catch (Exception e){

            }
        }

    }
}

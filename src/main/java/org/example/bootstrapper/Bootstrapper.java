package org.example.bootstrapper;

import org.example.Shell;
import org.example.network.DockerNetwork;
import org.example.node.NodeToNodeReceiver;
import org.example.udp.UdpManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Bootstrapper {
    private DockerNetwork dockerNetwork;
    public void run(){
        try{
            createNetwork();
            new Thread(new TcpListener()).start();
        }catch (Exception e){
            new RuntimeException(e);
        }
    }
    private void createNetwork() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        int containersNumber=2;
        for(int i=5000;i<5000+containersNumber;i++){
            try{
                UdpListener listener=new UdpListener(i);
                new Thread(listener).start();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        dockerNetwork=new DockerNetwork("NoSqlNetwork",containersNumber,"database-node");
    }
    private class TcpListener implements Runnable{
        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(3000);
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("New Connection At Port : "+socket.getPort());
                }
            } catch (IOException e) {

            }
        }
    }
    public class UdpListener implements Runnable{
        private DatagramSocket udpSocket;
        private byte[] buf = new byte[1024];
        public UdpListener(int port) throws SocketException {
            udpSocket = new DatagramSocket(port);
        }
        @Override
        public void run() {
            try {
                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);
                udpSocket.receive(packet);
                udpSocket.send(UdpManager.getInstance().execute(packet));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

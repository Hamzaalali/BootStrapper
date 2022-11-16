package org.example.bootstrapper;

import org.example.auth.User;
import org.example.network.DockerNetwork;
import org.example.port.PortsManager;
import org.example.tcp.UserConnection;
import org.example.udp.UdpManager;
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
    private void createNetwork() throws IOException, ExecutionException, InterruptedException, TimeoutException, ParseException {
        int containersNumber=3;
        for(int i=5000;i<5000+containersNumber;i++){
            try{
                PortsManager.getInstance().addPort(i);
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
                ServerSocket serverSocket = new ServerSocket(8080);
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("New Connection At Port : "+socket.getPort());
                    new Thread(new UserConnection(socket)).start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public class UdpListener implements Runnable{
        private DatagramSocket udpSocket;
        private int port;
        private byte[] buf = new byte[1024];
        public UdpListener(int port) throws SocketException {
            udpSocket = new DatagramSocket(port);
            this.port=port;
        }
        @Override
        public void run() {
            try {
                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);
                udpSocket.receive(packet);
                System.out.println("packet from : "+packet.getAddress().getHostAddress()+":"+packet.getPort());
                UdpManager.getInstance().execute(packet,udpSocket,port);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

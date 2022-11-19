package org.example.bootstrapper;
import org.example.env.EnvironmentVariables;
import org.example.network.DockerNetwork;
import org.example.node.Node;
import org.example.node.NodesManager;
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
        int tcpPort= EnvironmentVariables.getInstance().getTcpStartingRange();
        int udpPort=EnvironmentVariables.getInstance().getUdpStartingRange();
        int bootstrapperPort=EnvironmentVariables.getInstance().getBootstrapperUdpRange();
        for(int i=1;i<=EnvironmentVariables.getInstance().getContainerNumbers();i++,tcpPort++,udpPort++,bootstrapperPort++){
            try{
                Node node=new Node();
                node.setNodeNumber(i);
                node.setUdpPort(udpPort);
                node.setTcpPort(tcpPort);
                NodesManager.getInstance().addNode(node);
                UdpListener listener=new UdpListener(bootstrapperPort,i);
                new Thread(listener).start();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        dockerNetwork=new DockerNetwork("NoSqlNetwork","database-node");
    }
    private class TcpListener implements Runnable{
        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(EnvironmentVariables.getInstance().getBootstrapperTcpPort())){
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
        private byte[] buf = new byte[1024];
        private int nodeNumber;
        public UdpListener(int port,int nodeNumber) throws SocketException {
            udpSocket = new DatagramSocket(port);
            this.nodeNumber=nodeNumber;
        }
        @Override
        public void run() {
            try {
                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);
                udpSocket.receive(packet);
                UdpManager.getInstance().execute(packet,udpSocket,nodeNumber);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

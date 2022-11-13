package org.example.node;

import java.io.IOException;
import java.net.*;

public class NodeToNodeSender {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public NodeToNodeSender() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        System.out.println(address.getHostAddress());
    }

    public String sendEcho(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 4000);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }
}
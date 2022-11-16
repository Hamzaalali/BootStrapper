package org.example.udp.routine;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public abstract class UdpRoutine {

    public abstract void execute(DatagramPacket packet, DatagramSocket socket,int port );
}

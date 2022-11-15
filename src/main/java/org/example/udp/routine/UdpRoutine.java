package org.example.udp.routine;
import java.net.DatagramPacket;

public abstract class UdpRoutine {

    public abstract DatagramPacket execute(DatagramPacket packet,int port );
}

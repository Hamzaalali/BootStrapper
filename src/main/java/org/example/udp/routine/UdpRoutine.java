package org.example.udp.routine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.DatagramPacket;

public abstract class UdpRoutine {

    public abstract DatagramPacket execute(DatagramPacket packet);
}

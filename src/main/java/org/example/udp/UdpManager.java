package org.example.udp;
import org.example.udp.routine.RoutinesFactory;
import org.example.udp.routine.UdpRoutine;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.*;
import java.util.Map;

public class UdpManager {
    Map<CommandTypes, UdpRoutine> routineMap;
    private static UdpManager instance;

    private UdpManager(){
        RoutinesFactory routinesFactory=new RoutinesFactory();
        routineMap=routinesFactory.getRoutines();
    }
    public void execute(DatagramPacket packet,DatagramSocket socket,int nodeNumber) throws ParseException {
        String received
                = new String(packet.getData(), 0, packet.getLength());
        JSONParser jsonParser=new JSONParser();
        JSONObject routine= (JSONObject) jsonParser.parse(received);
        CommandTypes routineType= CommandTypes.valueOf((String) routine.get("commandType"));
        routineMap.get(routineType).execute(packet,socket,nodeNumber);
    }
    public void sendUdp(int port,String data) throws IOException {
        DatagramSocket socket=new DatagramSocket();
        DatagramPacket packet=new DatagramPacket(data.getBytes(),data.getBytes().length, InetAddress.getLocalHost(),port);
        socket.send(packet);
    }

    public static UdpManager getInstance() {
        if (instance == null) {
            instance = new UdpManager();
        }
        return instance;
    }
}

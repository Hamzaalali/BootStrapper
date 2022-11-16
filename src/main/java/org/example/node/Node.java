package org.example.node;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Node {
    private int nodeNumber;
    private int tcpPort;
    private JSONObject nodeJsonObject;
    private String ip;
    private int udpPort;

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public int getTcpPort() {
        return tcpPort;
    }
    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }
    public static Node of(JSONObject nodeObject){
        Node node=new Node();
        node.nodeNumber= (int)((long) nodeObject.get("nodeNumber"));
        node.ip= (String) nodeObject.get("ip");
        node.tcpPort= (int)((long) nodeObject.get("tcpPort"));
        node.nodeJsonObject=nodeObject;
        return node;
    }
    public JSONObject getNodeJsonObject(){
        return nodeJsonObject;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }
    public JSONObject toJson(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("ip",ip);
        jsonObject.put("tcpPort",tcpPort);
        jsonObject.put("nodeNumber",nodeNumber);
        jsonObject.put("udpPort",udpPort);
        return jsonObject;
    }
}

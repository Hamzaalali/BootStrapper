package org.example.network;

import org.example.Shell;
import org.example.node.NodeToNodeReceiver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.util.List;


public class DockerNetwork {
    private String subnetIp;
    private String gateway;
    private String broadCastAddress;
    private int cidr;
    private int tcpPort;
    private int udpPort;
    private int containersNumber;
    public DockerNetwork(JSONArray networkDetails,int containersNumber){
        JSONObject ipam= (JSONObject) ((JSONObject)networkDetails.get(0)).get("IPAM");
        JSONObject config=(JSONObject)((JSONArray)ipam.get("Config")).get(0);
        gateway= (String) config.get("Gateway");
        assignSubnetAndBroadcastAddress((String) config.get("Subnet"));
        tcpPort=3000;
        udpPort=4000;
        System.out.println(cidr);
        System.out.println(subnetIp);
        System.out.println(broadCastAddress);
        this.containersNumber=containersNumber;
        for(int i=udpPort;i<udpPort+containersNumber;i++){
            try{
                NodeToNodeReceiver node=new NodeToNodeReceiver(i+1000);
                new Thread(node).start();
            }catch (Exception e){

            }
        }
    }
    private void assignSubnetAndBroadcastAddress(String subnet){
        List<String > subnetAndCidr= List.of(subnet.split("/"));
        String cidr= subnetAndCidr.get(1);
        this.cidr=Integer.valueOf(cidr);
        subnetIp=subnetAndCidr.get(0);
        String[] ip=subnetIp.split("\\.");
        StringBuilder broadCastAddress=new StringBuilder();
        for(int i = 0, j = Integer.parseInt(cidr); i<4; i++){
            StringBuilder octant=new StringBuilder();
            String binaryIpOctant= String.format("%8s", Integer.toBinaryString(Integer.parseInt(ip[i]))).replace(' ', '0');
            for(int k=0;k<8;k++,j--){
                if(j>0){
                    octant.append(binaryIpOctant.charAt(k));
                }else{
                    octant.append("1");
                }
            }
            int integerOctant = Integer.parseInt(octant.toString(), 2);
            broadCastAddress.append(integerOctant);
            if(i!=3){
                broadCastAddress.append(".");
            }
        }
        this.broadCastAddress=broadCastAddress.toString();
    }
    public void addContainers(String imageName){
        for(int i=0;i<containersNumber;i++){
            try{
//                String runContainer=String.format("docker run -p %s:%s/udp -e BOOTSTRAPPER_PORT=%s -e GATEWAY_IP=%s --network=NoSqlNetwork -p %s:4000/udp -p %s:3000 -itd %s",udpPort+1000,udpPort+1000,String.valueOf(udpPort+1000),gateway,String.valueOf(udpPort),String.valueOf(tcpPort),imageName);
                String runContainer=String.format("docker run -e BOOTSTRAPPER_PORT=%s -e GATEWAY_IP=%s --network=NoSqlNetwork  -p %s:3000 -itd %s",String.valueOf(udpPort+1000),gateway,String.valueOf(tcpPort),imageName);

                System.out.println(runContainer);
                String id=Shell.getInstance().runShellCommand(runContainer);
                String getContainerIP=String.format("docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}'  %s",id);
                System.out.println(id);
                String containerIP=Shell.getInstance().runShellCommand(getContainerIP);
               containerIP= containerIP.substring(1,containerIP.length()-1);
               udpPort++;
               tcpPort++;
                System.out.println(containerIP);
            } catch ( Exception e) {
                System.out.println("Error while create container number :"+i);
                System.out.println(e);
            }
        }
    }
}

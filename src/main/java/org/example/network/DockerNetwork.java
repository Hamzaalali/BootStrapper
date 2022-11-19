package org.example.network;

import org.example.env.EnvironmentVariables;
import org.example.shll.Shell;
import org.example.node.NodesManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class DockerNetwork {
    private String broadCastAddress;

    public DockerNetwork(String networkName,String imageName) throws IOException, ExecutionException, InterruptedException, TimeoutException, ParseException {
        Shell.getInstance().runShellCommand(String.format("docker network create %s",networkName));
        assignSubnetAndBroadcastAddress();
        addContainers(imageName);
    }
    private void assignSubnetAndBroadcastAddress() throws ParseException, IOException, ExecutionException, InterruptedException, TimeoutException {
        String networkDetailsString= Shell.getInstance().runShellCommand("docker network inspect NoSqlNetwork");
        JSONParser jsonParser=new JSONParser();
        JSONArray networkDetails= (JSONArray) jsonParser.parse(networkDetailsString);
        JSONObject ipam= (JSONObject) ((JSONObject)networkDetails.get(0)).get("IPAM");
        JSONObject config=(JSONObject)((JSONArray)ipam.get("Config")).get(0);
        String gateway= (String) config.get("Gateway");
        String subnet= (String) config.get("Subnet");
        List<String > subnetAndCidr= List.of(subnet.split("/"));
        String cidr= subnetAndCidr.get(1);
        String subnetIp=subnetAndCidr.get(0);
        String[] ip=subnetIp.split("\\.");
        this.broadCastAddress= calculateBroadcastAddress(cidr, ip);
    }

    private static String calculateBroadcastAddress(String cidr, String[] ip) {
        StringBuilder broadCastAddress=new StringBuilder();
        for(int i = 0, j = Integer.parseInt(cidr); i<4; i++){
            StringBuilder octant=new StringBuilder();
            j = maskOctant(j, octant, ip[i]);
            int integerOctant = Integer.parseInt(octant.toString(), 2);
            broadCastAddress.append(integerOctant);
            if(i!=3){
                broadCastAddress.append(".");
            }
        }
        return broadCastAddress.toString();
    }

    private static int maskOctant(int j, StringBuilder octant, String ip) {
        String binaryIpOctant= String.format("%8s", Integer.toBinaryString(Integer.parseInt(ip))).replace(' ', '0');
        for(int k=0;k<8;k++, j--){
            if(j >0){
                octant.append(binaryIpOctant.charAt(k));
            }else{
                octant.append("1");
            }
        }
        return j;
    }

    public void addContainers(String imageName){
        int tcpPort= EnvironmentVariables.getInstance().getTcpStartingRange();
        int udpPort=EnvironmentVariables.getInstance().getUdpStartingRange();
        int bootstrapperPort=EnvironmentVariables.getInstance().getBootstrapperUdpRange();
        for(int i=1;i<=EnvironmentVariables.getInstance().getContainerNumbers();i++,tcpPort++,udpPort++,bootstrapperPort++){
            try{
                String runContainer=String.format("docker run -e NODE_NUMBER=%s -e UDP_PORT=%s -e BROADCAST_IP=%s -e BOOTSTRAPPER_PORT=%s  --network=NoSqlNetwork -p %s:4000/udp -p %s:3000 -itd %s",i,4000,broadCastAddress,bootstrapperPort,udpPort,tcpPort,imageName);
                String id=Shell.getInstance().runShellCommand(runContainer);
                String getContainerIP=String.format("docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}'  %s",id);
                System.out.println("container created with id :- "+id);
                String containerIP=Shell.getInstance().runShellCommand(getContainerIP);
                containerIP= containerIP.substring(1,containerIP.length()-1);
                NodesManager.getInstance().getNode(i).setIp(containerIP);
            } catch ( Exception e) {
                System.out.println("Error while create container number :"+i);
                System.out.println(e);
            }
        }
    }
}

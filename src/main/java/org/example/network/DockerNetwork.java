package org.example.network;

import org.example.Shell;


import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class DockerNetwork {
    private int tcpPort;
    private int udpPort;
    private int containersNumber;
    public DockerNetwork(String networkName,int containersNumber,String imageName) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Shell.getInstance().runShellCommand(String.format("docker network create %s",networkName));
        tcpPort=3000;
        udpPort=4000;
        this.containersNumber=containersNumber;

        addContainers(imageName);
    }
    public void addContainers(String imageName){
        for(int i=0;i<containersNumber;i++){
            try{
                String runContainer=String.format("docker run -e BOOTSTRAPPER_PORT=%s  --network=NoSqlNetwork -p %s:4000/udp -p %s:3000 -itd %s",String.valueOf(udpPort+1000),String.valueOf(udpPort),String.valueOf(tcpPort),imageName);
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

package org.example.port;
import java.util.ArrayList;
import java.util.List;

public class PortsManager {
    private List<Integer> ports;
    private static  PortsManager instance;
    private PortsManager(){
        ports=new ArrayList<>();
    }
    public void addPort(int port){
        ports.add(port);
    }
    public List<Integer> getPorts(){
        return ports;
    }
    public static PortsManager getInstance() {
        if (instance == null) {
            instance = new PortsManager();
        }
        return instance;
    }
}

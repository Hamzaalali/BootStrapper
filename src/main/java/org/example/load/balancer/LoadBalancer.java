package org.example.load.balancer;
import org.example.auth.User;
import org.example.auth.UsersManager;
import org.example.port.PortsManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBalancer {
    Map<Integer, List<User>> nodeUsers;
    private static LoadBalancer instance;
    private LoadBalancer(){
        nodeUsers=new HashMap<>();
        balance();
    }
    public Map<Integer,List<User>>balance(){
        List<Integer> ports= PortsManager.getInstance().getPorts();
        for(int i=0;i<ports.size();i++){
            nodeUsers.put(ports.get(i),new ArrayList<>());
        }
        List<User> userList= UsersManager.getInstance().getUsers();
        int portPointer=0;
        for(int i=0;i<userList.size();i++){
            nodeUsers.get(ports.get(portPointer)).add(userList.get(i));
            portPointer++;
            portPointer%=ports.size();
        }
        return nodeUsers;
    }
    public List<User> getPortUsers(int port){
        return nodeUsers.get(port);
    }
    public static LoadBalancer getInstance() {
        if (instance == null) {
            instance = new LoadBalancer();
        }
        return instance;
    }
}

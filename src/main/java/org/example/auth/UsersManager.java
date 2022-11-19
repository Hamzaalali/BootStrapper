package org.example.auth;

import org.example.file.system.DiskOperations;
import org.example.load.balancer.LoadBalancer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersManager {
    private static  UsersManager instance;
    List<User> users;
    private UsersManager(){
        users=new ArrayList<>();
    }
    public List<User> getUsers(){
        JSONArray usersArray= DiskOperations.readJsonArray("users");
        for(int i=0;i<usersArray.size();i++){
            users.add(User.of((JSONObject) usersArray.get(i)));
        }
        return users;
    }
    public int addUser(User user) throws IOException {
        users.add(user);
        int tcpPort=LoadBalancer.getInstance().balanceUser(user);
        DiskOperations.appendDocument("users",user.toJson());
        return tcpPort;
    }

    public static UsersManager getInstance() {
        if (instance == null) {
            instance = new UsersManager();
        }
        return instance;
    }
}

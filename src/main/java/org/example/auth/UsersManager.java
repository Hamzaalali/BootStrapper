package org.example.auth;

import java.util.ArrayList;
import java.util.List;

public class UsersManager {
    private static  UsersManager instance;
    private UsersManager(){

    }
    public List<User> getUsers(){
        List<User> users=new ArrayList<>();
        User user=new User();
        user.setUsername("admin");
        user.setPassword("admin");
        users.add(user);
        user=new User();
        user.setUsername("admin1");
        user.setPassword("admin1");
        users.add(user);
        user=new User();
        user.setUsername("admin2");
        user.setPassword("admin2");
        users.add(user);
        user=new User();
        user.setUsername("admin3");
        user.setPassword("admin3");
        users.add(user);
        return users;
    }
    public void addUser(User user){

    }

    public static UsersManager getInstance() {
        if (instance == null) {
            instance = new UsersManager();
        }
        return instance;
    }
}

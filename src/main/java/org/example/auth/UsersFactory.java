package org.example.auth;

import java.util.ArrayList;
import java.util.List;

public class UsersFactory {
    private static  UsersFactory instance;
    private UsersFactory(){

    }
    public List<User> getUsers(){
        List<User> users=new ArrayList<>();
        User user=new User();
        user.setUsername("admin");
        user.setPassword("admin");
        users.add(user);
        return users;
    }
    public static UsersFactory getInstance() {
        if (instance == null) {
            instance = new UsersFactory();
        }
        return instance;
    }
}

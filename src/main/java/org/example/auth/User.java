package org.example.auth;

import org.json.simple.JSONObject;

public class User {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public JSONObject toJson(){
        JSONObject userJson=new JSONObject();
        userJson.put("username",username);
        userJson.put("password",password);
        return userJson;
    }
    public static User of(JSONObject jsonObject){
        User user=new User();
        user.setUsername((String) jsonObject.get("username"));
        user.setPassword((String) jsonObject.get("password"));
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

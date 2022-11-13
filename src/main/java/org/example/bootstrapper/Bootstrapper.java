package org.example.bootstrapper;

import org.example.Shell;
import org.example.network.DockerNetwork;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Bootstrapper {
    private DockerNetwork dockerNetwork;

    public Bootstrapper(){
        try{
            init();
        }catch (Exception e){
            new RuntimeException(e);
        }
    }
    public void run(){

    }
    private void init() throws IOException, ExecutionException, InterruptedException, TimeoutException, ParseException {
       createNetwork();
       dockerNetwork.addContainers("database-node");

    }
    private void createNetwork() throws IOException, ExecutionException, InterruptedException, TimeoutException, ParseException {
        String buildNetwork="docker network create NoSqlNetwork";
        String dockerInspectNetwork="docker network inspect NoSqlNetwork";
        Shell.getInstance().runShellCommand(buildNetwork);
        JSONParser jsonParser=new JSONParser();
        JSONArray networkDetails= (JSONArray) jsonParser.parse(Shell.getInstance().runShellCommand(dockerInspectNetwork));
        dockerNetwork=new DockerNetwork(networkDetails,4);
    }
}

package org.example.tcp;

import org.example.node.ServerClientCommunicator;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.Socket;

public class UserConnection implements Runnable{
    private Socket socket;
    public UserConnection(Socket socket) throws IOException {
        this.socket = socket;
    }
    @Override
    public void run() {
        getUserRequests();
    }
    private void getUserRequests(){
        while(true){
            try {
                JSONObject request= ServerClientCommunicator.readJson(socket);
                ServerClientCommunicator.sendJson(socket,TcpManager.getInstance().execute(request));
            } catch (IOException e ) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
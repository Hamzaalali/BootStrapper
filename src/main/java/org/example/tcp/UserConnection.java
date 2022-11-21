package org.example.tcp;

import org.example.node.TCPCommunicator;
import org.json.simple.JSONObject;

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
                JSONObject request= TCPCommunicator.readJson(socket);
                TCPCommunicator.sendJson(socket,TcpManager.getInstance().execute(request));
            } catch (Exception e){
                //ignore connection reset
            }
        }
    }
}
package org.example.tcp;


import org.example.tcp.rqeuest.RequestFactory;
import org.example.tcp.rqeuest.TcpRequest;
import org.json.simple.JSONObject;

import java.util.Map;

public class TcpManager {
    Map<TcpRequestTypes, TcpRequest>requestMap;
    public TcpManager(){
        RequestFactory requestFactory=new RequestFactory();
        requestMap=requestFactory.getRequests();
    }
    private static TcpManager instance;
    public JSONObject execute(JSONObject request){
        TcpRequestTypes routineType= TcpRequestTypes.valueOf((String) request.get("requestType"));
        return requestMap.get(routineType).execute(request);
    }

    public static TcpManager getInstance() {
        if (instance == null) {
            instance = new TcpManager();
        }
        return instance;
    }
}

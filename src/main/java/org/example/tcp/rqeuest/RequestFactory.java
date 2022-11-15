package org.example.tcp.rqeuest;
import org.example.tcp.TcpRequestTypes;
import java.util.HashMap;
import java.util.Map;

public class RequestFactory {
    public Map<TcpRequestTypes, TcpRequest> getRequests(){
        Map<TcpRequestTypes,TcpRequest> requestMap=new HashMap<>();
        requestMap.put(TcpRequestTypes.REGISTER,new RegisterRequest());
        return requestMap;
    }
}

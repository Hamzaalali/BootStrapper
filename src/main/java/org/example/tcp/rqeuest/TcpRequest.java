package org.example.tcp.rqeuest;

import org.json.simple.JSONObject;

public abstract class TcpRequest {
    public abstract JSONObject execute(JSONObject jsonObject);
}

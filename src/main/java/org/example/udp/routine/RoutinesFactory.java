package org.example.udp.routine;
import org.example.udp.CommandTypes;

import java.util.HashMap;
import java.util.Map;

public class RoutinesFactory {
    public Map<CommandTypes,UdpRoutine> getRoutines(){
        Map<CommandTypes,UdpRoutine> routineMap=new HashMap<>();
        routineMap.put(CommandTypes.INITIALIZE,new InitlializeRoutine());
        return routineMap;
    }
}

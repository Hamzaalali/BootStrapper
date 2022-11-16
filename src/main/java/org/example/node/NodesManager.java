package org.example.node;


import java.util.ArrayList;
import java.util.List;

public class NodesManager {
    private List<Node> nodes;
    private static NodesManager instance;
    private NodesManager(){
        nodes=new ArrayList<>();
    }
    public void addNode(Node node){
        nodes.add(node);
    }
    public List<Node> getNodes(){
        return nodes;
    }
    public Node getNode(int index){
        return nodes.get(index);
    }
    public static NodesManager getInstance() {
        if (instance == null) {
            instance = new NodesManager();
        }
        return instance;
    }
}

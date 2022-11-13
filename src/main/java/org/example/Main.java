package org.example;

import org.example.bootstrapper.Bootstrapper;
import org.example.node.NodeToNodeReceiver;
import org.example.node.NodeToNodeSender;

import java.io.IOException;
import java.net.InetAddress;


public class Main {
    public static void main(String[] args) throws IOException {
        Bootstrapper bootstrapper=new Bootstrapper();
        bootstrapper.run();

    }
}
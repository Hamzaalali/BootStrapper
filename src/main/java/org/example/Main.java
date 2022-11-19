package org.example;

import org.example.bootstrapper.Bootstrapper;
import org.example.env.EnvironmentVariables;

import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args)  {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Welcome To NoSql Bootstrapping CLI");
        System.out.println("tcp ports starting range :- ");
        int tcpStartingRange=scanner.nextInt();//4000
        System.out.println("udp ports starting range :- ");
        int udpStartingRange=scanner.nextInt();//5000
        System.out.println("bootstrapper udp ports starting range :- ");
        int bootstrapperUdpRange=scanner.nextInt();//6000
        System.out.println("load balance time window :- ");
        int loadBalanceTimeWindow=scanner.nextInt();//5
        System.out.println("load balance max requests :- ");
        int loadBalanceMaxRequests=scanner.nextInt();//5
        System.out.println("number of containers :- ");
        int containerNumbers=scanner.nextInt();//6
        System.out.println("bootstrapper tcp port :- ");
        int bootstrapperTcpPort=scanner.nextInt();//8080
        System.out.println("wait till containers are made ! thanks for using the cli");
        System.out.println("--------------------------------------");
        EnvironmentVariables.getInstance().setTcpStartingRange(tcpStartingRange);
        EnvironmentVariables.getInstance().setUdpStartingRange(udpStartingRange);
        EnvironmentVariables.getInstance().setBootstrapperUdpRange(bootstrapperUdpRange);
        EnvironmentVariables.getInstance().setLoadBalanceTimeWindow(loadBalanceTimeWindow);
        EnvironmentVariables.getInstance().setLoadBalanceMaxRequests(loadBalanceMaxRequests);
        EnvironmentVariables.getInstance().setContainerNumbers(containerNumbers);
        EnvironmentVariables.getInstance().setBootstrapperTcpPort(bootstrapperTcpPort);
        Bootstrapper bootstrapper=new Bootstrapper();
        bootstrapper.run();
    }
}
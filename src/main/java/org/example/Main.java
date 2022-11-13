package org.example;

import org.example.bootstrapper.Bootstrapper;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Bootstrapper bootstrapper=new Bootstrapper();
        bootstrapper.run();
    }
}
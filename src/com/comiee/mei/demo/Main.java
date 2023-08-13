package com.comiee.mei.demo;

import com.comiee.mei.communication.Client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Client client = new DemoClient();
        new Thread(client::listenServer).start();
        String s;
        Scanner scan = new Scanner(System.in);
        do {
            s = scan.nextLine();
            Object ret = client.send(new DebugMsg().build(s));
            System.out.println(ret);
        } while (!s.isEmpty());
        client.close();
    }
}

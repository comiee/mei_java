package com.comiee.mei.demo;

import com.comiee.mei.communal.exception.LoadException;
import com.comiee.mei.communication.Client;
import com.google.gson.JsonElement;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws LoadException {
        Client client = new DemoClient();
        new Thread(client::listenServer).start();
        String s;
        Scanner scan = new Scanner(System.in);
        do {
            s = scan.nextLine();
            JsonElement ret = client.send(new DebugMsg().build(s));
            System.out.println(ret);
        } while (!s.isEmpty());
        client.close();
    }
}

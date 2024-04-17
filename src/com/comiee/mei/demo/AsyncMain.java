package com.comiee.mei.demo;

import com.comiee.mei.communal.JsonTool;
import com.comiee.mei.communication.AsyncClient;
import com.google.gson.JsonObject;

public class AsyncMain {
    public static void main(String[] args) {
        Thread thread2 = AsyncClient.start("debug", (client) -> {
            String ret = (String) client.send("2");
            System.out.println(ret);
        });
        Thread thread1 = AsyncClient.start("debug", (client) -> {
            String ret = (String) client.send("1");
            System.out.println(ret);
        });
        Thread thread0 = AsyncClient.start("h_pic", (client) -> {
            JsonObject obj = JsonTool.createObject("r18", 2);
            JsonObject ret = (JsonObject) client.send(obj);
            System.out.println(ret);
        });
        try {
            thread2.join();
            thread1.join();
            thread0.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

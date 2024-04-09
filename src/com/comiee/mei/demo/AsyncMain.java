package com.comiee.mei.demo;

import com.comiee.mei.communication.AsyncClient;

public class AsyncMain {
    public static void main(String[] args) {
        Thread thread2 = AsyncClient.start("debug", (client) -> {
            String ret = client.send("2");
            System.out.println(ret);
        });
        Thread thread1 = AsyncClient.start("debug", (client) -> {
            String ret = client.send("1");
            System.out.println(ret);
        });
        try {
            thread2.join();
            thread1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

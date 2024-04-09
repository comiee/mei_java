package com.comiee.mei.communication;

import java.io.IOException;
import java.net.Socket;


class Comm {
    static final String HOST = "192.168.1.108";
    static final int PORT = 9999;
    static final int ASYNC_PORT = 9998;
    private static final String ENCODING = "UTF-8";

    public static final int RECONNECT_TIME = 3; // 连接失败后的重连等待时间

    static void sendMsg(Socket socket, String string) throws IOException {
        var output = socket.getOutputStream();
        var message = string.getBytes(ENCODING);
        var length = String.format("%d", message.length).getBytes(ENCODING);
        var n = String.format("%05d", length.length).getBytes(ENCODING);
        output.write(n);
        output.write(length);
        output.write(message);
    }

    static String recvMsg(Socket socket) throws IOException {
        var input = socket.getInputStream();
        int n = Integer.parseInt(new String(input.readNBytes(5), ENCODING));
        int length = Integer.parseInt(new String(input.readNBytes(n), ENCODING));
        return new String(input.readNBytes(length), ENCODING);
    }

    static void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignored) {
        }
    }
}

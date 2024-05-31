package com.comiee.mei.communication;

import java.io.IOException;
import java.net.Socket;


class Comm {
    static final String HOST = "172.17.176.1";
    static final int PORT = 9999;
    static final int ASYNC_PORT = 9998;
    static final String ENCODING = "UTF-8";

    public static final int RECONNECT_TIME = 3; // 连接失败后的重连等待时间

    static synchronized void sendBytes(Socket socket, byte[] message) throws IOException {
        var output = socket.getOutputStream();
        var length = String.format("%d", message.length).getBytes(ENCODING);
        var n = String.format("%05d", length.length).getBytes(ENCODING);
        output.write(n);
        output.write(length);
        output.write(message);
    }

    static void sendMsg(Socket socket, String string) throws IOException {
        sendBytes(socket, string.getBytes(ENCODING));
    }

    static synchronized byte[] recvBytes(Socket socket) throws IOException {
        var input = socket.getInputStream();
        int n = Integer.parseInt(new String(input.readNBytes(5), ENCODING));
        int length = Integer.parseInt(new String(input.readNBytes(n), ENCODING));
        return input.readNBytes(length);
    }

    static String recvMsg(Socket socket) throws IOException {
        return new String(recvBytes(socket), ENCODING);
    }

    static void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignored) {
        }
    }
}

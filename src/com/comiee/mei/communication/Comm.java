package com.comiee.mei.communication;

import java.io.IOException;
import java.net.Socket;


class Comm {
    static final String HOST = "127.0.0.1";
    static final int PORT = 9999;
    private static final String ENCODING = "UTF-8";

    public static final int RECONNECT_TIME = 3; // 连接失败后的重连等待时间

    static void sendMsg(Socket socket, String string) throws IOException {
        var output = socket.getOutputStream();
        var message = string.getBytes(ENCODING);
        output.write(String.format("%05d", message.length).getBytes(ENCODING));
        output.write(message);
    }

    static String recvMsg(Socket socket) throws IOException {
        var input = socket.getInputStream();
        int length = Integer.parseInt(new String(input.readNBytes(5), ENCODING));
        return new String(input.readNBytes(length), ENCODING);
    }

    static void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignored) {
        }
    }
}

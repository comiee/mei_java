package com.comiee.mei.communication;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static com.comiee.mei.communication.Comm.*;

public class AsyncClient {
    private final String cmd;
    private Socket sock;

    private static final Logger logger = Logger.getLogger("asyncClient");

    public AsyncClient(String cmd) {
        this.cmd = cmd;
        connect();
    }

    private void connect() {
        try {
            sock = new Socket(HOST, ASYNC_PORT);
            sendMsg(sock, cmd);
            recvMsg(sock);
            logger.fine("异步客户端" + cmd + "建立连接");
        } catch (IOException e) {
            logger.severe("异步客户端" + cmd + "连接服务器失败：" + e);
        }
    }

    public String send(String message) {
        try {
            sendMsg(sock, message);
            logger.fine("异步客户端" + cmd + "向服务器发送消息：" + message);
            String ret = recvMsg(sock);
            logger.fine("异步客户端" + cmd + "收到服务器响应：" + ret);
            return ret;
        } catch (IOException e) {
            logger.severe("异步客户端" + cmd + "连接服务器失败：" + e);
            return "";
        }
    }

    public void close() {
        try {
            sock.close();
        } catch (IOException ignored) {
        }
    }

    public static Thread start(String cmd, Consumer<AsyncClient> fun) {
        return Thread.startVirtualThread(() -> {
            AsyncClient client = new AsyncClient(cmd);
            fun.accept(client);
            client.close();
        });
    }
}

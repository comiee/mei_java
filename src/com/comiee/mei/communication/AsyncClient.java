package com.comiee.mei.communication;

import com.comiee.mei.communal.JsonTool;
import com.google.gson.JsonObject;

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
            String ret = (String) send(cmd);
            if (!ret.equals("success")) {
                throw new IOException("服务器未注册的命令字。");
            }
            logger.fine("异步客户端" + cmd + "建立连接");
        } catch (IOException e) {
            logger.severe("异步客户端" + cmd + "连接服务器失败：" + e);
        }
    }

    private String messageEncode(Object obj) throws Exception {
        if (obj instanceof String) {
            return "str:" + obj;
        } else if (obj instanceof JsonObject) {
            return "json:" + obj;
        } else {
            throw new Exception("不支持发送的类型" + obj.getClass());
        }
    }

    private Object messageDecode(String message) throws Exception {
        String[] args = message.split(":", 2);
        String type = args[0];
        String val = args[1];
        if (type.equals("str")) {
            return val;
        } else if (type.equals("json")) {
            return JsonTool.parse(val);
        } else {
            throw new Exception("不支持接受的类型" + type);
        }
    }

    public Object send(Object obj) {
        try {
            String message = messageEncode(obj);
            sendMsg(sock, message);
            logger.fine("异步客户端" + cmd + "向服务器发送消息：" + message);
            String ret = recvMsg(sock);
            logger.fine("异步客户端" + cmd + "收到服务器响应：" + ret);
            return messageDecode(ret);
        } catch (IOException e) {
            logger.severe("异步客户端" + cmd + "连接服务器失败：" + e);
        } catch (Exception e) {
            logger.severe("异步客户端" + cmd + "发送消息时出错：" + e);
        }
        return "";
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

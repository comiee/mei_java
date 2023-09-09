package com.comiee.mei.communication;

import com.comiee.mei.communal.exception.MessageException;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import static com.comiee.mei.communication.Comm.*;

public class Client {
    private final String name;
    private Socket sender;
    private Socket receiver;

    private final Logger logger = Logger.getLogger("client");

    public Client(String name) {
        this.name = name;
    }

    /**
     * 向服务器注册
     *
     * @param clientType 可选项有：sender、receiver
     */
    private Socket register(String clientType) throws IOException {
        logger.info("客户端" + name + "正在向服务器注册" + clientType);
        // 创建Socket对象，指定服务端的IP地址和端口号
        Socket socket = new Socket(HOST, PORT);
        sendMsg(socket, new RegisterMsg().build(name, clientType));
        logger.info("客户端[" + name + "] " + clientType + " 成功连接服务器");
        return socket;
    }

    public JsonElement send(String message) {
        try {
            if (sender == null) {
                sender = register("sender");
            }
            sendMsg(sender, message);
            logger.fine("客户端[" + name + "]发送消息到服务器：" + message);
            String result = recvMsg(sender);
            logger.fine("客户端[" + name + "]收到服务器回响应：" + result);
            return ResultMsg.parseResult(result);
        } catch (MessageException e) {
            logger.severe("客户端[" + name + "]解析服务器响应消息失败：" + e);
            return null;
        } catch (IOException e) {
            logger.warning("客户端[" + name + "]连接服务器失败：" + e + "，将在" + RECONNECT_TIME + "秒后重试");
            sleep(RECONNECT_TIME);
            return send(message);
        }
    }

    public void listenServer() {
        try {
            receiver = register("receiver");
            while (receiver.isConnected()) {
                String message = recvMsg(receiver);
                logger.fine("客户端[" + name + "]收到服务器消息：" + message);
                Object value;
                try {
                    value = Message.parse(message);
                } catch (MessageException e) {
                    logger.severe("客户端[" + name + "]解析服务器消息失败：" + e);
                    continue;
                }
                String result = new ResultMsg().build(value);
                logger.fine("客户端[" + name + "]向服务器回响应：" + result);
                sendMsg(receiver, result);
            }
        } catch (IOException e) {
            logger.severe("客户端[" + name + "监听服务器时出现异常：" + e + "，将在" + RECONNECT_TIME + "秒后重试");
            sleep(RECONNECT_TIME);
            listenServer();
        }
    }

    public void close() {
        if (sender != null) {
            try {
                sender.close();
            } catch (IOException ignored) {
            }
        }
        if (receiver != null) {
            try {
                receiver.close();
            } catch (IOException ignored) {
            }
        }
    }
}

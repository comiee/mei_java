package com.comiee.mei.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

    public <R> R send(String message) throws IOException {
        if (sender == null) {
            sender = register("sender");
        }
        // TODO 重试机制
        sendMsg(sender, message);
        logger.fine("客户端[" + name + "]发送消息到服务器：" + message);
        String result = recvMsg(sender);
        logger.fine("客户端[" + name + "]收到服务器回响应：" + result);
        return Message.parse(result);
    }

    public <T, R> void listenServer() {
        try {
            receiver = register("receiver");
            while (receiver.isConnected()) {
                String message = recvMsg(receiver);
                logger.fine("客户端[" + name + "]收到服务器消息：" + message);
                String result = new ResultMsg<T, R>().build(Message.parse(message));
                logger.fine("客户端[" + name + "]向服务器回响应：" + result);
                sendMsg(receiver, result);
            }
        } catch (IOException e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            logger.severe("监听客户端时出现异常：" + e + "\n" + stringWriter);
        }
    }

    public void close() throws IOException {
        if (sender != null) {
            sender.close();
        }
        if (receiver != null) {
            receiver.close();
        }
    }
}

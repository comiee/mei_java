package com.comiee.mei.communication;

import com.comiee.mei.communal.Json;
import com.comiee.mei.communal.Receiver;


/**
 * 消息类
 * 发送{"cmd":cmd, "value":value}
 * 接收{"cmd":"result", "value":value}
 *
 * @param <T> 该消息的value类型
 * @param <R> 响应消息的value类型
 */
public abstract class Message<T, R> {
    private String cmd; // 消息命令字

    private Receiver<T, R> receiver;

    protected Message(String cmd) {
        this.cmd = cmd;
    }

    /**
     * 构建消息
     *
     * @param value 消息内容
     * @return json格式的字符串
     */
    protected String buildMsg(T value) {
        return Json.of(
                "cmd", cmd,
                "value", value
        ).toString();
    }

    /* 注册接收消息时的处理函数 */
    public void on_receive(Receiver<T, R> receiver) {
        this.receiver = receiver;
    }

    static <R> R parse(String message) {
        return null; // TODO 实现
    }
}

class RegisterMsg extends Message<Json, Object> {
    RegisterMsg() {
        super("register");
    }

    String build(String name, String type) {
        return super.buildMsg(Json.of(
                "name", name,
                "client_type", type
        ));
    }
}

class ResultMsg<T, R> extends Message<T, R> {
    ResultMsg() {
        super("result");
    }

    String build(T value) {
        return super.buildMsg(value);
    }
}
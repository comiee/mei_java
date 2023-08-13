package com.comiee.mei.communication;

import com.comiee.mei.communal.Json;
import com.comiee.mei.communal.Receiver;
import com.comiee.mei.communal.exception.MessageException;

import java.util.HashMap;
import java.util.Map;


/**
 * 消息类
 * 发送{"cmd":cmd, "value":value}
 * 接收{"cmd":"result", "value":value}
 */
public abstract class Message {
    private static final Map<String, Message> messageMap = new HashMap<>();

    private final String cmd; // 消息命令字
    private Receiver receiver;

    protected Message(String cmd) {
        this.cmd = cmd;
    }

    /**
     * 构建消息
     *
     * @param value 消息内容
     * @return json格式的字符串
     */
    protected String buildMsg(Object value) {
        return Json.of(
                "cmd", cmd,
                "value", value
        ).toString();
    }


    /* 注册接收消息时的处理函数 */
    public void on_receive(Receiver receiver) {
        this.receiver = receiver;
        messageMap.put(cmd, this);
    }

    private Object solve(Object value) {
        return receiver.receive(value);
    }

    static Object parse(String message) throws MessageException {
        Json json = Json.parse(message);
        if (!(json.get("cmd") instanceof String cmd)) {
            throw new MessageException("解析消息出错，不存在cmd字段或cmd不是String：" + message);
        }
        Object value = json.get("value");

        for (String s : messageMap.keySet()) {
            if (s.equals(cmd)) {
                return messageMap.get(s).solve(value);
            }
        }
        throw new MessageException("解析消息出错，未注册的命令：" + message);
    }
}

class RegisterMsg extends Message {
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

class ResultMsg extends Message {
    ResultMsg() {
        super("result");
    }

    String build(Object value) {
        return super.buildMsg(value);
    }

    static Object parseResult(String message) throws MessageException {
        Json json = Json.parse(message);
        if (!(json.get("cmd") instanceof String cmd)) {
            throw new MessageException("解析响应消息出错，不存在cmd字段或cmd不是String：" + message);
        }
        if (!cmd.equals("result")) {
            throw new MessageException("解析响应消息出错，cmd不是result：" + message);
        }
        return json.get("value");
    }
}
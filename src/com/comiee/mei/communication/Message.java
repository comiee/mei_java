package com.comiee.mei.communication;

import com.comiee.mei.communal.JsonTool;
import com.comiee.mei.communal.exception.MessageException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 消息类
 * 发送{"cmd":cmd, "value":value}
 * 接收{"cmd":"result", "value":value}
 */
public class Message {
    private static final Map<String, Message> messageMap = new HashMap<>();

    private final String cmd; // 消息命令字

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
        return JsonTool.createObject(
                "cmd", cmd,
                "value", value
        ).toString();
    }


    /* 注册接收消息时的处理函数 */
    @SuppressWarnings("unused")
    private void register() { // 此函数通过反射调用
        messageMap.put(cmd, this);
    }

    public Object receive(JsonElement value) {
        return null;
    }

    static Object parse(String message) throws MessageException {
        JsonObject jsonObject = JsonTool.parse(message);
        String cmd = JsonTool.getFromObject(jsonObject, "cmd", String.class);
        if (cmd == null) {
            throw new MessageException("解析消息出错，不存在cmd字段或cmd不是String：" + message);
        }
        JsonElement value = JsonTool.getFromObject(jsonObject, "value", JsonElement.class);
        if (value == null) {
            throw new MessageException("解析消息出错，不存在value字段" + message);
        }

        for (String s : messageMap.keySet()) {
            if (s.equals(cmd)) {
                return messageMap.get(s).receive(value);
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
        return super.buildMsg(JsonTool.createObject(
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

    static JsonElement parseResult(String message) throws MessageException {
        JsonObject jsonObject = JsonTool.parse(message);
        String cmd = JsonTool.getFromObject(jsonObject, "cmd", String.class);
        if (cmd == null) {
            throw new MessageException("解析消息出错，不存在cmd字段或cmd不是String：" + message);
        }
        if (!cmd.equals("result")) {
            throw new MessageException("解析响应消息出错，cmd不是result：" + message);
        }
        JsonElement value = JsonTool.getFromObject(jsonObject, "value", JsonElement.class);
        if (value == null) {
            throw new MessageException("解析消息出错，不存在value字段" + message);
        }
        return value;
    }
}
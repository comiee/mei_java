package com.comiee.mei.demo;

import com.comiee.mei.communication.Message;
import com.google.gson.JsonElement;

public class DebugMsg extends Message {
    public DebugMsg() {
        super("debug");
    }

    public String build(String value) {
        return super.buildMsg(value);
    }

    @Override
    public Object receive(JsonElement value) {
        System.out.println("调测信息：" + value);
        return value;
    }
}

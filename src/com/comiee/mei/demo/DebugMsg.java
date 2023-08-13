package com.comiee.mei.demo;

import com.comiee.mei.communication.Message;

public class DebugMsg extends Message {
    static { // TODO 这种注册方式有点麻烦，尝试使用反射注册
        new DebugMsg().register();
    }

    DebugMsg() {
        super("debug");
    }

    String build(String value) {
        return super.buildMsg(value);
    }

    @Override
    public Object receive(Object value) {
        System.out.println("调测信息：" + value);
        return value;
    }
}

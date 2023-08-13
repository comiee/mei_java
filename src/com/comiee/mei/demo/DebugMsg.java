package com.comiee.mei.demo;

import com.comiee.mei.communal.Receiver;
import com.comiee.mei.communication.Message;

public class DebugMsg extends Message implements Receiver {
    static { // TODO 这种注册方式有点麻烦，尝试使用反射注册
        DebugMsg debugMsg = new DebugMsg();
        debugMsg.on_receive(debugMsg);
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

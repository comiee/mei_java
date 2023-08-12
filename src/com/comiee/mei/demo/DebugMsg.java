package com.comiee.mei.demo;

import com.comiee.mei.communal.Receiver;
import com.comiee.mei.communication.Message;

public class DebugMsg extends Message<String, String> implements Receiver<String, String> {
    static {
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
    public String receive(String value) {
        System.out.println(value);
        return value;
    }
}

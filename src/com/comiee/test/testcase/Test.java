package com.comiee.test.testcase;


import com.comiee.mei.communication.Client;
import com.comiee.mei.demo.DebugMsg;
import com.comiee.mei.demo.DemoClient;
import com.comiee.test.comm.TestCase;

public class Test extends TestCase {
    private void testMultiMsg() throws Exception {
        Client client = new DemoClient();
        client.send(new DebugMsg().build("test"));
        Thread[] ts = new Thread[30];
        for (int i = 0; i < ts.length; i++) {
            int fi = i;
            ts[i] = new Thread(() -> {
                client.send(new DebugMsg().build("" + fi));
            });
        }
        for (Thread t : ts) {
            t.start();
        }
        for (Thread t : ts) {
            t.join();
        }
    }

    public static void main(String[] args) throws Exception {
        var testCase = new Test();
        testCase.testMultiMsg();
    }
}

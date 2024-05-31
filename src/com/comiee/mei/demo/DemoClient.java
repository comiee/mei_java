package com.comiee.mei.demo;

import com.comiee.mei.communal.exception.LoadException;
import com.comiee.mei.communication.Client;
import com.comiee.mei.initialization.Load;


public class DemoClient extends Client {
    public DemoClient() throws LoadException {
        super("demo_java");
        Load.initMessage();
    }
}

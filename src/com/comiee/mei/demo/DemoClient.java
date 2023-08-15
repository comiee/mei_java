package com.comiee.mei.demo;

import com.comiee.mei.communal.exception.LoadException;
import com.comiee.mei.communication.Client;
import com.comiee.mei.initialization.Load;


class DemoClient extends Client {
    DemoClient() throws LoadException {
        super("demo");
        Load.initMessage();
    }
}

package com.comiee.mei.initialization;

import com.comiee.mei.communal.exception.LoadException;
import com.comiee.mei.communal.reflect.ReflectTool;
import com.comiee.mei.communication.Message;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Logger;


public class Load {
    private static final Logger logger = Logger.getLogger("load");

    public static void initMessage() throws LoadException {
        initMessage("com");
    }

    private static void initMessage(String pack) throws LoadException {
        // 获取包名下所有类
        Set<Class<?>> classes = ReflectTool.getClasses(pack);
        for (var c : classes) {
            if (!c.equals(Message.class) && Message.class.isAssignableFrom(c) && ReflectTool.hasMethod(c, "receive")) {
                loadMessage(c);
            }
        }
    }

    private static void loadMessage(Class<?> c) throws LoadException {
        try {
            Constructor<?> constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            Message instance = (Message) constructor.newInstance();
            Method registerMethod = Message.class.getDeclaredMethod("register");
            registerMethod.setAccessible(true);
            registerMethod.invoke(instance);
            logger.info("加载消息成功：" + c.getName());
        } catch (Exception e) {
            String msg = "加载消息失败：" + c.getName() + "，错误：" + e;
            logger.severe(msg);
            LoadException loadException = new LoadException(msg);
            loadException.setStackTrace(e.getStackTrace());
            throw loadException;
        }
    }
}


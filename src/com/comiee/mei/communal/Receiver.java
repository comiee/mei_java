package com.comiee.mei.communal;

@FunctionalInterface
public interface Receiver<T, R> {
    /**
     * 接收到消息时执行
     *
     * @param data 消息内容
     * @return 消息的响应
     */
    R receive(T data);
}
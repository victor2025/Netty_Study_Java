package com.victor2022.netty.dubborpc.provider;

import com.victor2022.netty.dubborpc.netty.NettyServer;

/**
 * @author victor2022
 * @creat 2022/5/12 16:50
 * 服务提供者的启动类，会启动服务的提供者服务器
 */
public class ServerBootStrap {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",8000);
    }
}

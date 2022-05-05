package com.victor2022.nio.niotest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author: victor2022
 * @date: 2022/5/2 下午4:29
 * @description: NIO客户端
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {
        // 获取一个socketChannel
        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞
        socketChannel.configureBlocking(false);
        // 设置服务端地址和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8000);
        // 连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            // 若未完成连接，则当前客户端可以先去做其他事情
            while(!socketChannel.finishConnect()){
                System.out.println("在等待建立连接的过程中，客户端可以先去做其他事情");
            }
        }

        // 若连接成功，则发送数据
        String str = "hello from "+socketChannel.getLocalAddress();
        // 将数据包装到Buffer中
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 通过channel发送数据
        socketChannel.write(buffer);
        System.in.read();
    }
}

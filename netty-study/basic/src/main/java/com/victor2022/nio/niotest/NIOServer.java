package com.victor2022.nio.niotest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: victor2022
 * @date: 2022/5/2 下午4:10
 * @description: NIO服务器
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        // 创建 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 创建一个Selector对象
        Selector selector = Selector.open();

        // 绑定端口，开启监听
        serverSocketChannel.socket().bind(new InetSocketAddress(8000));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 将serverSocketChannel注册到selector，关注的事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while(true){
            // 等待一秒，若没有事件发生，则跳过接下来的过程
            if(selector.select(1000)==0){
                // 当前没有事件发生
                System.out.println("当前时间为："+new Date().getTime()+" 没有客户端连接...");
                continue;
            }
            // 若有事件发生，则获取相关的selectionKey集合
            // 通过selectionKey反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历selectionKey集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while(iterator.hasNext()){
                // 获取当前的selectionKey
                SelectionKey key = iterator.next();
                // 根据key获取相应通道发生的事件，并进行处理
                // 若为连接事件
                if(key.isAcceptable()){
                    // 若为指定事件，生成socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel："+socketChannel.hashCode());
                    // 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将当前channel注册到selector，关注事件为OP_READ，同时关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                // 若为读事件
                if(key.isReadable()){
                    // 反向获取channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int read = channel.read(buffer);
                    if(read!=-1) System.out.println(new String(buffer.array(),0,read)+" from channel：" +channel.hashCode());
                    buffer.clear();
                }
                // 手动移除selectionKey，防止重复操作
                iterator.remove();
            }
        }
    }
}

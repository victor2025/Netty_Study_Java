package com.victor2022.netty.groupchat;

import com.victor2022.netty.groupchat.handler.GroupChatClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午9:53
 * @description:
 */
public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午9:54
     * @description: 开启服务端
     */
    public void run(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new GroupChatClientInitializer());
            System.out.println("GroupChatClient based on Netty is Started...");
            ChannelFuture cf = bootstrap.connect(host, port).sync();
            // 获取channel
            Channel channel = cf.channel();
            System.out.println("Client address is: "+channel.localAddress());
            // 创建扫描器，获取输入信息
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNextLine()){
                String msg = scanner.nextLine();
                // 通过channel发送至服务端
                channel.writeAndFlush(msg+"\r\n");
            }
            // 监听关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new GroupChatClient("localhost",8000).run();
    }
}

package com.victor2022.netty.groupchat;

import com.victor2022.netty.groupchat.handler.GroupChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午8:58
 * @description: 基于netty的群聊系统的服务器
 */
public class GroupChatServer {

    private int port;

    public GroupChatServer(int port){
        this.port = port;
    }

    /**
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午9:01
     * @description: 主方法，用来处理客户端的请求
     */
    public void run(){
        // 创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            // 配置引导类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new GroupChatServerInitializer());
            // 启动服务器
            System.out.println("GroupChatServer based on Netty is Started...");
            ChannelFuture cf = bootstrap.bind(port).sync();
            // 监听关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new GroupChatServer(8000).run();

    }

}

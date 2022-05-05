package com.victor2022.netty.heartbeat;

import com.victor2022.netty.heartbeat.handler.HeartbeatServerHandler;
import com.victor2022.netty.heartbeat.handler.HeartbeatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午10:30
 * @description: 基于netty的心跳检测服务器
 */
public class HeartbeatServer {

    private final int port;

    public HeartbeatServer(int port) {
        this.port = port;
    }
    
    public void run(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HeartbeatServerInitializer());
            // 启动服务器
            System.out.println("HeartbeatServer based on Netty is Started...");
            ChannelFuture cf = bootstrap.bind(this.port).sync();
            // 监视关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HeartbeatServer(8000).run();
    }
}

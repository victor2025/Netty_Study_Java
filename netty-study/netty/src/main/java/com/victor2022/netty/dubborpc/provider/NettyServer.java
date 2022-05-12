package com.victor2022.netty.dubborpc.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author victor2022
 * @creat 2022/5/12 16:52
 */
public class NettyServer {

    /**
     * @param hostname:
     * @param port:
     * @return: void
     * @author: lihen
     * @date: 2022/5/12 17:06
     * @description: 开启服务器的方法
     */
    public static void startServer(String hostname, int port){
        startServer0(hostname,port);
    }

    /**
     * @param hostname:
     * @param port:
     * @return: void
     * @author: lihen
     * @date: 2022/5/12 17:06
     * @description: 启动服务器的方法
     */
    private static void startServer0(String hostname, int port){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            // 自己的业务处理器
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            // 启动
            ChannelFuture cf = bootstrap.bind(hostname, port).sync();
            System.out.println("Service provider is started...");
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

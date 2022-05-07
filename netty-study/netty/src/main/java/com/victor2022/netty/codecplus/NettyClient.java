package com.victor2022.netty.codecplus;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @author: victor2022
 * @date: 2022/5/3 下午3:55
 * @description: 客户端
 */
public class NettyClient {

    public static void main(String[] args) {
        // 需要一个事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try{
            // 创建客户端启动对象
            // 使用BootStrap(注意和ServerBootStrap区分)
            Bootstrap bootstrap = new Bootstrap();
            // 设置参数
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        // 添加处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 添加protobuf编码器
                            pipeline.addLast("encoder",new ProtobufEncoder());
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("Netty Client is ready");
            // 启动客户端连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8000).sync();
            // 监听关闭通道
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            group.shutdownGracefully();
        }
    }
}

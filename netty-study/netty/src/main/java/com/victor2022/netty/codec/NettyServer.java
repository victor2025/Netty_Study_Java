package com.victor2022.netty.codec;

import com.victor2022.netty.codec.pojo.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @author: victor2022
 * @date: 2022/5/3 下午3:28
 * @description: 基于netty的tcp服务
 */
public class NettyServer {

    public static void main(String[] args) {
        // 创建BossGroup和WorkerGroup，均为无限循环
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            // 创建服务器的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置参数
            bootstrap.group(bossGroup,workerGroup)// 设置线程组
                    .channel(NioServerSocketChannel.class)// 设置channel类型
                    .option(ChannelOption.SO_BACKLOG,128)// 线程队列连接数目
                    .childOption(ChannelOption.SO_KEEPALIVE,true)// 保持连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 给workerGroup的EventLoop对应的管道设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 添加protobuf解码器
                            // 需要指定被解码的对象
                            pipeline.addLast("decoder",new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("Netty TCP Server is ready!");

            // 绑定端口并同步，生成一个ChannelFuture对象
            // 启动服务器
            ChannelFuture cf = bootstrap.bind(8000).sync();

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            // 关闭两个group
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

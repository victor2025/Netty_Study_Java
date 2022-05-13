package com.victor2022.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: victor2022
 * @date: 2022/5/12 下午7:57
 * @description:
 */
public class NettyClient {

    // 创建线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;

    // 使用代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass, final String prefix){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},(proxy, method, args) -> {
                    // 客户端每执行一次业务方法，都会执行以下代码
                    if(client==null){
                        initClient();
                    }
                    String aim = serviceClass.getName()+"#"+method.getName()+"#";
                    // 设置要发给服务器的信息
                    client.setPara(prefix+aim+args[0]);
                    return executor.submit(client).get();
                });
    }

    /**
     * @return: void
     * @author: victor2022
     * @date: 2022/5/12 下午8:02
     * @description: 初始化客户端
     */
    private static void initClient(){
        client = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            // 自定义处理器
                            pipeline.addLast(client);
                        }
                    });
            bootstrap.connect("127.0.0.1", 8000).sync();
            System.out.println("Client is ready...");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

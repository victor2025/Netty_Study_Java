package com.victor2022.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author: victor2022
 * @date: 2022/5/3 下午5:11
 * @description:
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 向管道中加入处理器
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 加入netty提供的http编解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        // 添加自定义handler
        pipeline.addLast("MyHttpServerHandler",new HttpServerHandler());
    }
}

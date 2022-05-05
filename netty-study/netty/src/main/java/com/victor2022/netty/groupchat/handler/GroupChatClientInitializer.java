package com.victor2022.netty.groupchat.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午9:56
 * @description: 客户端初始化类
 */
public class GroupChatClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 获取pipeline
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 加入相关handler
        pipeline.addLast("decoder",new StringDecoder());
        pipeline.addLast("encoder",new StringEncoder());
        pipeline.addLast(new GroupChatClientHandler());
    }
}

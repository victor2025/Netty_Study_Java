package com.victor2022.netty.groupchat.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午9:06
 * @description:
 */
public class GroupChatServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 获取pipeline
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 向pipeline中添加handler
        // 添加解码器
        pipeline.addLast("decoder",new StringDecoder());
        // 添加编码器
        pipeline.addLast("encoder",new StringEncoder());
        // 添加自定义业务处理handler
        pipeline.addLast(new GroupChatServerHandler());

    }

}

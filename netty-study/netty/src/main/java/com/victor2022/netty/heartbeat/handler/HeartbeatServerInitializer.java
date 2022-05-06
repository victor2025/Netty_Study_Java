package com.victor2022.netty.heartbeat.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午10:34
 * @description:
 */
public class HeartbeatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 添加一个netty提供的IdleStateHandler
        /*
         * 说明
         * 1. IdleStateHandler 是 netty 提供的处理空闲状态的处理器
         * 2. long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接
         * 3. long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接
         * 4. long allIdleTime : 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接
         * 5. 文档说明
         * triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
         * read, write, or both operation for a while.
         * 6. 当 IdleStateEvent 触发后 , 就会传递给管道 的下一个 handler 去处理
         * 通过调用(触发)下一个 handler 的 userEventTriggered , 在该方法中去处理 IdleStateEvent(读
         * 空闲，写空闲，读写空闲)
         */
        pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
        // 加入对空闲检测的进一步处理
        pipeline.addLast(new HeartbeatServerHandler());
    }
}

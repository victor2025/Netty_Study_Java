package com.victor2022.netty.groupchat.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午9:58
 * @description:
 */
public class GroupChatClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * @param channelHandlerContext:
     * @param s:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午10:10
     * @description: 出现读事件
     * 收到消息就打印出来
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s.trim());
    }
}

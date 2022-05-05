package com.victor2022.netty.groupchat.handler;

import com.victor2022.netty.groupchat.utils.MsgUtils;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午9:09
 * @description:
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 使用ChannelGroup保存所有channel，并方便操作
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    /**
     * @param ctx:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午9:17
     * @description: 在建立连接后，第一个执行该方法
     * 添加该连接的channel到channelGroup，并通知其他客户端
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户端发送的信息推送给所有其他在线的客户端
        // 使用channelGroup完成
        channelGroup.writeAndFlush(MsgUtils.clientConnected(channel.remoteAddress()));
        channelGroup.add(channel);
    }

    /**
     * @param ctx:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午9:23
     * @description: 断开连接后执行
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(MsgUtils.clientDisconnected(channel.remoteAddress()));
        System.out.println("channelGroup size: "+channelGroup.size());
    }

    /**
     * @param ctx:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午9:37
     * @description: 客户端活动状态执行
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.print(MsgUtils.clientOnline(ctx.channel().remoteAddress()));
    }

    /**
     * @param ctx:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午9:41
     * @description: 客户端非活动状态执行
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.print(MsgUtils.clientOffline(ctx.channel().remoteAddress()));
    }

    /**
     * @param channelHandlerContext:
     * @param s:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午9:41
     * @description: 读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        // 获取当前channel
        Channel channel = channelHandlerContext.channel();
        // 遍历channelGroup，根据情况回复不同消息
        channelGroup.forEach(ch->{
            if(channel!=ch){
                // 不是当前channel，转发消息
                ch.writeAndFlush(MsgUtils.clientTextOthers(channel.remoteAddress(),s));
            }else{
                // 是当前channel，回显自己发送了消息
                ch.writeAndFlush(MsgUtils.clientTextSelf(s));
            }
        });
    }

    /**
     * @param ctx:
     * @param cause:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午9:49
     * @description: 捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
        cause.printStackTrace();
    }
}

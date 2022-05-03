package com.victor2022.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author: victor2022
 * @date: 2022/5/3 下午4:00
 * @description: 客户端处理handler
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * @param ctx:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 下午4:01
     * @description: 当通道就绪后，触发该方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client "+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, server!",CharsetUtil.UTF_8));
    }

    /**
     * @param ctx:
     * @param msg:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 下午4:04
     * @description: 响应读事件
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息是：" +buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }

    /**
     * @param ctx:
     * @param cause:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 下午4:04
     * @description: 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

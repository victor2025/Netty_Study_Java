package com.victor2022.netty.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author: victor2022
 * @date: 2022/5/6 下午9:30
 * @description: TextWebSocketFrame表示一个文本帧
 * 针对文本帧frame进行处理
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println("服务器收到消息: "+textWebSocketFrame.text());

        // 回复消息
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("服务器时间"
                + LocalDateTime.now()
                + " "
                + textWebSocketFrame.text()));
    }

    /**
     * @param ctx:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/6 下午9:38
     * @description: 客户端连接后，触发方法
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved被调用"+ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生"+cause.getMessage());
        ctx.close();
    }
}

package com.victor2022.netty.protocoltcp.handler;

import com.victor2022.netty.protocoltcp.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author victor2022
 * @creat 2022/5/7 16:15
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        // 接收数据，进行处理
        int len = messageProtocol.getLen();
        byte[] content = messageProtocol.getContent();

        System.out.println("服务器接收到如下信息：");
        System.out.println("长度："+len);
        System.out.println("内容："+new String(content, StandardCharsets.UTF_8));
        System.out.println("服务器收到的信息包数目："+(++this.count)+"\n");

        // 回复消息
        String resContent = UUID.randomUUID().toString();
        byte[] resBytes = resContent.getBytes(StandardCharsets.UTF_8);
        int resLen = resBytes.length;
        // 构建协议包
        MessageProtocol resMessageProtocol = new MessageProtocol();
        resMessageProtocol.setLen(resLen);
        resMessageProtocol.setContent(resBytes);
        channelHandlerContext.writeAndFlush(resMessageProtocol);
    }
}

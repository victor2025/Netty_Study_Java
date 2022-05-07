package com.victor2022.netty.protocoltcp.handler;

import com.victor2022.netty.protocoltcp.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author victor2022
 * @creat 2022/5/7 16:03
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常消息："+cause.getMessage());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        int len = messageProtocol.getLen();
        byte[] content = messageProtocol.getContent();
        System.out.println("客户端收到如下消息：");
        System.out.println("长度："+len);
        System.out.println("内容："+new String(content, StandardCharsets.UTF_8));
        System.out.println("客户端接收消息数量："+(++this.count)+"\n");
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端发送10条数据
        String msg = "测试自定义协议1,2,3,4,5,6";
        for (int i = 0; i < 5; i++) {
            byte[] content = msg.getBytes(StandardCharsets.UTF_8);
            int length = content.length;
            // 创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }
}

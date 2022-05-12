package com.victor2022.netty.dubborpc.netty;

import com.victor2022.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author victor2022
 * @creat 2022/5/12 16:57
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    final String prefix = "HelloService#hello#";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息，并调用服务

        System.out.println("msg: "+msg);
        // 客户端在调用服务端api的时候，需要定义一个协议
        // 定义每次发消息的时候都必须以某个字符串开头
        // "HelloService#hello#"
        String str = msg.toString();
        if(str.startsWith(prefix)){
            // 若msg满足协议
            String response = new HelloServiceImpl().hello(str.substring(prefix.length()));
            // 数据返回
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

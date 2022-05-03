package com.victor2022.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author: victor2022
 * @date: 2022/5/3 下午5:15
 * @description: 自定义处理器
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * @param channelHandlerContext:
     * @param httpObject:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 下午5:16
     * @description: 读取客户端数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        // 判断是否为http请求
        if(httpObject instanceof HttpRequest){
            HttpRequest httpRequest = (HttpRequest) httpObject;
            // 获取uri，过滤指定资源
            URI uri = new URI(httpRequest.uri());
            if(uri!=null&&uri.getPath().endsWith(".ico")){
                System.out.println("请求了: "+uri.getPath()+"，不做响应");
                return;
            }
            System.out.println("请求了: " +uri.getPath());
            // 返回信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("Hello, this is HttpServer", CharsetUtil.UTF_8);
            // 构造http响应
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            // 返回响应
            channelHandlerContext.writeAndFlush(response);

        }
    }
}

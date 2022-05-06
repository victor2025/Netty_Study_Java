package com.victor2022.netty.websocket.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author: victor2022
 * @date: 2022/5/6 下午9:23
 * @description:
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 添加Http的编解码器
        pipeline.addLast(new HttpServerCodec());
        // 块方式编写
        pipeline.addLast(new ChunkedWriteHandler());
        // http传输会多段聚合，添加相关处理器
        pipeline.addLast(new HttpObjectAggregator(8192));
        /*
        说明
        1. 对应 websocket ，它的数据是以 帧(frame) 形式传递
        2. 可以看到 WebSocketFrame 下面有六个子类
        3. 浏览器请求时 ws://localhost:8000/hello 表示请求的 uri
        4. WebSocketServerProtocolHandler 核心功能是将 http 协议升级为 ws 协议 , 保持长连接
        5. 是通过一个 状态码 101
        */
        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

        // 自定义handler
        pipeline.addLast(new MyTextWebSocketFrameHandler());
    }
}

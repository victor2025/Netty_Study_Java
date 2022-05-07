package com.victor2022.netty.protocoltcp.codec;

import com.victor2022.netty.protocoltcp.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author victor2022
 * @creat 2022/5/7 16:12
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("自定义解码器被调用");
        // 将二进制字节码放入数据包对象中
        int len = byteBuf.readInt();
        byte[] content = new byte[len];
        byteBuf.readBytes(content);

        // 封装为MessageProtocol对象，交给下一个handler处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(len);
        messageProtocol.setContent(content);

        list.add(messageProtocol);
    }
}

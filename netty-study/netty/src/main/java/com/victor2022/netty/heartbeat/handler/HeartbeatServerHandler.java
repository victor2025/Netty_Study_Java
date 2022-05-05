package com.victor2022.netty.heartbeat.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午10:38
 * @description:
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * @param ctx:
     * @param evt:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/5 下午10:48
     * @description: 对用户事件进行处理
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 对心跳事件进一步处理
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress()+"--超时时间--"+eventType);
            System.out.println("服务器进行处理");
        }
    }

}

package com.victor2022.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author victor2022
 * @creat 2022/5/12 17:09
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result;
    private String para;

    /**
     * @return: void
     * @author: lihen
     * @date: 2022/5/12 17:11
     * @description: 连接创建后调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 在其他方法会使用ctx，因此先保存下来
        this.context = ctx;
        System.out.println("channel activated...");
    }

    /**
     * @param ctx:
     * @param msg:
     * @return: void
     * @author: lihen
     * @date: 2022/5/12 17:12
     * @description: 收到服务器返回后调用
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.result = msg.toString();
        // 方法的调用和返回都是call方法来进行的
        // 唤醒等待的call方法线程
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * @return: java.lang.Object
     * @author: lihen
     * @date: 2022/5/12 17:15
     * @description: 被代理对象调用
     * 发送数据给服务器 --> wait 并等待被唤醒 --> 被唤醒后返回结果
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("before call");
        System.out.println("发送消息："+para);
        this.context.writeAndFlush(para);
        // 等待数据返回，channelRead方法获取到服务器的结果后，被唤醒wait();
        wait();
        System.out.println("after call");
        // 返回结果
        return result;
    }

    /**
     * @param para:
     * @return: void
     * @author: lihen
     * @date: 2022/5/12 17:19
     * @description: 设置请求参数
     */
    public void setPara(String para){
        this.para = para;
    }
}

package com.victor2022.netty.codecplus;

import com.victor2022.netty.codecplus.pojo.MyDataInfo;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author: victor2022
 * @date: 2022/5/3 下午3:42
 * @description: 实际处理handler
 * 需要继承netty规定的适配器
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * @param ctx: 上下文对象，包含管道，通道，地址等等
     * @param msg: 客户端发送的数据
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 下午3:46
     * @description: 读取数据事件
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收protobuf
        if(msg instanceof MyDataInfo.MyMessage){
            MyDataInfo.MyMessage element = (MyDataInfo.MyMessage) msg;
            MyDataInfo.MyMessage.DataType dataType = element.getDataType();
            if(dataType==MyDataInfo.MyMessage.DataType.StudentType){
                MyDataInfo.Student student = element.getStudent();
                System.out.println("客户端发送的student："
                        + "\nid:"+student.getId()
                        + "\nname:"+student.getName());
            }else if(dataType==MyDataInfo.MyMessage.DataType.WorkerType){
                MyDataInfo.Worker worker = element.getWorker();
                System.out.println("客户端发送的worker："
                        + "\nage:"+worker.getAge()
                        + "\nname:"+worker.getName());
            }else{
                System.out.println("类型错误");
            }
        }
    }

    /**
     * @param ctx:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 下午3:52
     * @description: 数据读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // write+flush
        // 向缓存中写入数据，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, client!",CharsetUtil.UTF_8));
    }

    /**
     * @param ctx:
     * @param cause:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 下午3:54
     * @description: 异常处理,关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

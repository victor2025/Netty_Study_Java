package com.victor2022.netty.dubborpc.netty;

import com.victor2022.netty.dubborpc.provider.DogServiceImpl;
import com.victor2022.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

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
            // 获取类路径和方法名
            String classpathAndArgs = str.substring(prefix.length());
            // 解析
            String[] arr = classpathAndArgs.split("#");
            String classpath = arr[0];
            String methodName = arr[1];
            String args = classpathAndArgs.substring(classpath.length()+1+methodName.length()+1);
            // 通过反射，生成对应类的对象，并调用，返回方法
            String response = null;
            if(classpath.toLowerCase().contains("helloservice")){
                HelloServiceImpl helloService = new HelloServiceImpl();
                Method method = helloService.getClass().getDeclaredMethod(methodName,String.class);
                response = method.invoke(helloService,args).toString();
            }else if(classpath.toLowerCase().contains("dogservice")){
                DogServiceImpl dogService = new DogServiceImpl();
                response = dogService.getClass().getMethod(methodName, String.class).invoke(dogService,args).toString();
            }
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

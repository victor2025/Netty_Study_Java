package com.victor2022.netty.dubborpc.provider;

import com.victor2022.netty.dubborpc.publicinterface.HelloService;

/**
 * @author victor2022
 * @creat 2022/5/12 16:48
 * 服务提供方被调用的实际方法
 */
public class HelloServiceImpl implements HelloService {

    /**
     * @param msg:
     * @return: java.lang.String
     * @author: lihen
     * @date: 2022/5/12 16:48
     * @description: 消费方调用该方法，则返回一个结果
     */
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息："+msg);
        // 根据msg返回不同结果
        if(msg!=null){
            return "Hello client, your message is received: "+msg;
        }else{
            return "Hello client, your message is null!";
        }
    }

}

package com.victor2022.netty.dubborpc.client;

import com.victor2022.netty.dubborpc.netty.NettyClient;
import com.victor2022.netty.dubborpc.publicinterface.DogService;
import com.victor2022.netty.dubborpc.publicinterface.HelloService;

/**
 * @author: victor2022
 * @date: 2022/5/12 下午8:11
 * @description: 消费者主方法
 */
public class ClientBootStrap {

    public static final String prefix = "HelloService#hello#";

    public static void main(String[] args) throws InterruptedException {
        // 创建一个消费者
        NettyClient customer = new NettyClient();
        // 生成公有接口的代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, prefix);
        // 通过代理对象调用提供者的方法或者服务
        System.out.println(service.hello("Hello"));
        Thread.sleep(2000);
        // 生成DogService的代理对象
        DogService dogService = (DogService) customer.getBean(DogService.class, prefix);
        System.out.println(dogService.bark("汪汪汪!"));
        System.out.println(dogService.name("Keven"));

    }
}

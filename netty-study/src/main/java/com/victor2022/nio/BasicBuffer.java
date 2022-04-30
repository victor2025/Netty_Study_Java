package com.victor2022.nio;

import java.nio.IntBuffer;

/**
 * @author: victor2022
 * @date: 2022/4/30 下午9:33
 * @description: NIO中Buffer的说明
 */
public class BasicBuffer {

    public static void main(String[] args) {
        // 创建Buffer
        IntBuffer intBuffer = IntBuffer.allocate(5);
        // 向Buffer中存放数据
        for(int i = 0; i<intBuffer.capacity(); i++){
            intBuffer.put(i*2);
        }

        // 从Buffer中读取数据
        // 将Buffer转换为读取状态，读写转换
        intBuffer.flip();
        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }

}

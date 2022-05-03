package com.victor2022.nio.basicchannel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: victor2022
 * @date: 2022/5/1 下午3:29
 * @description: 使用channel和buffer读取文件
 */
public class FileRead {

    public static void main(String[] args) throws IOException {
        // 创建文件输入流
        FileInputStream fis = new FileInputStream("res/hello.txt");
        // 获取输入channel
        FileChannel channel = fis.getChannel();
        // 创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 读取文件里面的数据
        int read = channel.read(buffer);
        // 还原为字符串
        if(read!=-1){
            System.out.println(new String(buffer.array(),0,read));
        }
        fis.close();
    }
}

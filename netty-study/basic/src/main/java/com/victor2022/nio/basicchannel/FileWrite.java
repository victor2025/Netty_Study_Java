package com.victor2022.nio.basicchannel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: victor2022
 * @date: 2022/5/1 下午3:18
 * @description: 使用channel和buffer写入文件
 */
public class FileWrite {

    public static void main(String[] args) throws IOException {
        // 创建文件输出流
        FileOutputStream fos = new FileOutputStream("res/hello.txt");
        // 获取文件channel
        FileChannel channel = fos.getChannel();
        // 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 写入字节数组
        byteBuffer.put("Hello world!".getBytes());
        // 转为输出类型
        byteBuffer.flip();
        // 开始输出
        channel.write(byteBuffer);
        fos.close();
    }

}

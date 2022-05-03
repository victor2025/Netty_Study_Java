package com.victor2022.nio.basicchannel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: victor2022
 * @date: 2022/5/1 下午4:01
 * @description: 使用channel和buffer拷贝数据
 */
public class FileCopy {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("res/hello.txt");
        FileChannel fisChannel = fis.getChannel();

        FileOutputStream fos = new FileOutputStream("res/hello_copy.txt");
        FileChannel fosChannel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while(true){
            // 清空buffer，一定要做，否则陷入死循环
            buffer.clear();
            // 读取源文件
            int read = fisChannel.read(buffer);
            if(read==-1){
                // 原文件读取完毕
                break;
            }
            // 将buffer中数据写入新文件中
            // 反转为输入状态
            buffer.flip();
            fosChannel.write(buffer);
        }
        // 关闭对应的流
        fis.close();
        fos.close();
    }
}

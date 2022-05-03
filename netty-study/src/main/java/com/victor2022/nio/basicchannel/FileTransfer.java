package com.victor2022.nio.basicchannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author: victor2022
 * @date: 2022/5/1 下午4:15
 * @description: 使用transferFrom复制文件
 */
public class FileTransfer {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("res/hello.txt");
        FileOutputStream fos = new FileOutputStream("res/hello_copy2.txt");

        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        fosChannel.transferFrom(fisChannel,0,fisChannel.size());

        fis.close();
        fos.close();
    }
}

package com.victor2022.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: victor2022
 * @date: 2022/4/30 下午8:44
 * @description: BIO服务端示例
 */
public class BIOServer {

    /**
     * @param args:
     * @return: void
     * @author: victor2022
     * @date: 2022/4/30 下午9:02
     * @description: 使用线程池实现BIO服务端
     */
    public static void main(String[] args) throws IOException {
        // 1. 创建一个线程池
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(4, 6, 5000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.DiscardPolicy());

        // 3. 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("服务已启动...");
        while(true){
            // 监听，等待客户端链接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端: "+socket.getInetAddress()+":"+socket.getPort());

            // 使用线程池启动线程，与之通信
            poolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    // 与客户端通信
                    handler(socket);
                }
            });
        }
    }

    /**
     * @param socket:
     * @return: void
     * @author: victor2022
     * @date: 2022/4/30 下午8:57
     * @description: 通信handler，实现具体通信方法
     */
    public static void handler(Socket socket){
        InputStream is = null;
        try{
            byte[] bytes = new byte[1024];
            // 通过socket获取输入流
            is = socket.getInputStream();
            // 循环读取客户端发送的数据
            while(true){
                int read = is.read(bytes);
                if(read!=-1){
                    // 输出客户端发送的数据
                    System.out.println(socket.getInetAddress()+":"+socket.getPort()+"$:"+new String(bytes,0,read));
                }else{
                    // 读取完毕，跳出
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

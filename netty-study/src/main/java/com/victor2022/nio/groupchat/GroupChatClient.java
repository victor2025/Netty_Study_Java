package com.victor2022.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author: victor2022
 * @date: 2022/5/3 上午10:06
 * @description: 群聊应用客户端
 */
public class GroupChatClient {

    private final String HOST="127.0.0.1";
    private final int PORT = 8000;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws IOException {
        this.selector = Selector.open();
        // 连接服务器
        this.socketChannel = SocketChannel.open(new InetSocketAddress(this.HOST, this.PORT));
        this.socketChannel.configureBlocking(false);
        // 注册到selector
        this.socketChannel.register(selector, SelectionKey.OP_READ);
        // 得到username
        this.username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+" is ready!");
    }

    /**
     * @param msg:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 上午10:13
     * @description: 向服务器发送数据
     */
    public void sendMsg(String msg){
        msg = this.username+"说：" + msg;
        try{
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 上午10:15
     * @description: 从服务器获取数据
     */
    public void readMsg(){
        try{
            int cnt = selector.select();
            if(cnt>0){
                // 当前有事件发生
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        // 获得相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 获取一个Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 读取数据
                        int read = sc.read(buffer);
                        // 将数据转为字符串
                        if(read>0){
                            String msg = new String(buffer.array(),0,read);
                            System.out.println(msg.trim());
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // 启动客户端
        GroupChatClient client = new GroupChatClient();
        // 启动一个线程，每3秒读取一次数据
        new Thread(){

            @Override
            public void run() {
                while(true){
                    client.readMsg();
                    try{
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }.start();
        // 向服务器发送数据
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            client.sendMsg(s);
        }
    }
}

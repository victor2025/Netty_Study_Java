package com.victor2022.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author: victor2022
 * @date: 2022/5/2 下午5:59
 * @description: 群聊系统的服务器
 */
public class GroupChatServer {

    private Selector selector;

    private ServerSocketChannel listenChannel;

    private int port = 8000;

    /**
     * @return: null
     * @author: victor2022
     * @date: 2022/5/2 下午6:03
     * @description: 初始化
     */
    public GroupChatServer(int port) {
        this.port = port;
        initServer(new InetSocketAddress(this.port));
    }

    public GroupChatServer(){
        initServer(new InetSocketAddress(this.port));
    }

    /**
     * @param address:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/2 下午6:40
     * @description: 初始化服务器
     */
    private void initServer(InetSocketAddress address){
        try{
            // 创建selector
            this.selector = Selector.open();
            // 创建serverSocketChannel
            this.listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(address);
            // 设置为非阻塞
            listenChannel.configureBlocking(false);
            // 将此listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @return: void
     * @author: victor2022
     * @date: 2022/5/3 上午11:01
     * @description: 监听事件
     */
    public void listen(){
        try{
            while(true){
                int count = selector.select();
                if(count>0){
                    // 当前有事件要处理
                    // 遍历selectionKey
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        // 取出当前selectionKey
                        SelectionKey key = iterator.next();

                        // 监听客户端连接
                        if(key.isAcceptable()){
                            // 建立连接
                            buildConnection();
                        }

                        // 监听客户端发送信息
                        if(key.isReadable()){
                            // 处理读
                            readData(key);
                        }
                        // 删除当前的key，防止重复操作
                        iterator.remove();
                    }
                }else{
                    System.out.println("监听事件中");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return: void
     * @author: victor2022
     * @date: 2022/5/2 下午6:21
     * @description: 建立连接
     */
    private void buildConnection() throws IOException {
        SocketChannel socketChannel = listenChannel.accept();
        socketChannel.configureBlocking(false);
        // 注册当前channel到selector
        socketChannel.register(selector,SelectionKey.OP_READ);
        // 命令行提示
        System.out.println(socketChannel.getRemoteAddress().toString().substring(1)+"上线啦！");
    }

    /**
     * @param key:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/2 下午6:20
     * @description: 读取客户端消息
     */
    private void readData(SelectionKey key) {
        // 获取相关的channel
        SocketChannel channel = null;
        try{
            // 得到channel
            channel = (SocketChannel) key.channel();
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            // 读取客户端数据
            int read = channel.read(buffer);
            // 进行数据处理
            if(read >0){
                // 将缓冲区的数据转为字符串
                String msg = new String(buffer.array(),0, read);
                // 输出消息
                System.out.println(msg);
                // 向其他客户端转发消息
                sendMsgToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            try{
                System.out.println(channel.getRemoteAddress()+"离线了");
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @param msg:
     * @param channel:
     * @return: void
     * @author: victor2022
     * @date: 2022/5/2 下午6:30
     * @description: 向除了channel之外的客户端发送消息
     */
    private void sendMsgToOtherClients(String msg, SocketChannel channel) throws IOException {
        System.out.println("转发消息中...");
        // 遍历所有socketChannel，并排除当前channel
        for(SelectionKey key: selector.keys()){
            // 通过key获取对应的SocketChannel
            Channel target = key.channel();
            // 获取指定类型channel，并排除自身
            if(target instanceof SocketChannel&&target!=channel){
                // 转型
                SocketChannel targetChannel = (SocketChannel) target;
                // 将msg存入buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer写入通道
                targetChannel.write(buffer);
            }
        }
        System.out.println("转发完毕...");
    }

    public static void main(String[] args) {
        // 创建服务器，并开始监听
        GroupChatServer groupChatServer = new GroupChatServer(8000);
        groupChatServer.listen();
    }

}

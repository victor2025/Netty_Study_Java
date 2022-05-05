package com.victor2022.netty.groupchat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: victor2022
 * @date: 2022/5/5 下午9:25
 * @description: 用于生成消息的工具类
 */
public class MsgUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String CLIENT_LABEL = "[客户端]";
    private static final String SELF_LABEL = "[自己]";
    private static final String SERVER_LABEL = "[服务端]";
    /**
     * @param username:
     * @return: java.lang.String
     * @author: victor2022
     * @date: 2022/5/5 下午9:27
     * @description: 客户端建立连接消息
     */
    public static String clientConnected(Object username){
        return CLIENT_LABEL+username.toString()+" 加入聊天! "
                +sdf.format(new Date())+"\n";
    }

    /**
     * @param username:
     * @return: java.lang.String
     * @author: victor2022
     * @date: 2022/5/5 下午9:32
     * @description: 客户端断开连接消息
     */
    public static String clientDisconnected(Object username){
        return CLIENT_LABEL+username.toString()+" 离开了\n";
    }

    /**
     * @param username:
     * @return: java.lang.String
     * @author: victor2022
     * @date: 2022/5/5 下午9:38
     * @description: 上线消息
     */
    public static String clientOnline(Object username){
        return CLIENT_LABEL+username.toString()+" 上线了！\n";
    }

    /**
     * @param username:
     * @return: java.lang.String
     * @author: victor2022
     * @date: 2022/5/5 下午9:43
     * @description: 下线消息
     */
    public static String clientOffline(Object username){
        return CLIENT_LABEL+username.toString()+" 离线了！\n";
    }

    /**
     * @param username:
     * @param msg:
     * @return: java.lang.String
     * @author: victor2022
     * @date: 2022/5/5 下午9:43
     * @description: 转发消息正文
     */
    public static String clientTextOthers(Object username, String msg){
        return CLIENT_LABEL+username.toString()+" 发送了消息: "+msg+"\n";
    }

    /**
     * @param msg:
     * @return: java.lang.String
     * @author: victor2022
     * @date: 2022/5/5 下午9:46
     * @description: 自己发送的消息正文
     */
    public static String clientTextSelf(String msg){
        return SELF_LABEL+" 发送了消息: "+msg+"\n";
    }

}

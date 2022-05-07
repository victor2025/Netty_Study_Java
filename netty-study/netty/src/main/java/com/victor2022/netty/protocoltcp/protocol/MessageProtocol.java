package com.victor2022.netty.protocoltcp.protocol;

/**
 * @author victor2022
 * @creat 2022/5/7 16:02
 * 协议包主体
 */
public class MessageProtocol {
    private int len;
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}

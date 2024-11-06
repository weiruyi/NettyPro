package com.ruyi.netty.protocoltcp;

/**
 * 协议包
 */
public class MessageProtocol {
    private int len;
    private byte[] content;

    public MessageProtocol() {
    }

    public MessageProtocol(int len, byte[] content) {
        this.len = len;
        this.content = content;
    }

    /**
     * 获取
     * @return len
     */
    public int getLen() {
        return len;
    }

    /**
     * 设置
     * @param len
     */
    public void setLen(int len) {
        this.len = len;
    }

    /**
     * 获取
     * @return content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * 设置
     * @param content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    public String toString() {
        return "MessageProtocol{len = " + len + ", content = " + content + "}";
    }
}

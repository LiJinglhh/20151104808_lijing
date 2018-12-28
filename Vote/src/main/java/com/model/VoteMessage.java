package com.model;

import java.io.Serializable;

public class VoteMessage implements Serializable {
    /**
     * 唯一标识
     */
    private int messageId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 参与项目id
     */
    private int batchId;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    @Override
    public String toString() {
        return "VoteMessage{" +
                "messageId=" + messageId +
                ", userName='" + userName + '\'' +
                ", batchId='" + batchId + '\'' +
                '}';
    }
}

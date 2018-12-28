package com.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class VoteBatch implements Serializable{
    /**
     * 项目id
     */
    private int batchId;

    /**
     * 项目名称
     */
    private String batchName;

    /**
     * 项目分类
     */
    private String batchGroup;

    /**
     * 问题个数
     */
    private int questionNum;

    /**
     * 参与人数
     */
    private int userNum;

    /**
     * 投票截止时间
     */
    private Date endTime;

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getBatchGroup() {
        return batchGroup;
    }

    public void setBatchGroup(String batchGroup) {
        this.batchGroup = batchGroup;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "VoteBatch{" +
                "batchId=" + batchId +
                ", batchName='" + batchName + '\'' +
                ", batchGroup='" + batchGroup + '\'' +
                ", questionNum=" + questionNum +
                ", userNum=" + userNum +
                ", endTime=" + endTime +
                '}';
    }
}

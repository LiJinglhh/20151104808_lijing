package com.model;

import java.io.Serializable;

public class VoteQuestion implements Serializable {
    /**
     * 问题id
     */
    private int questionId;

    /**
     * 问题内容
     */
    private String questionName;

    /**
     * 所属项目id
     */
    private int questionBatchId;

    /**
     * a选项内容
     */
    private String questionA;

    /**
     * b选项内容
     */
    private String questionB;

    /**
     * c选项内容
     */
    private String questionC;

    /**
     * d选项内容
     */
    private String questionD;

    /**
     * a选项选择人数
     */
    private int numberA;

    /**
     * b选项选择人数
     */
    private int numberB;

    /**
     * c选项选择人数
     */
    private int numberC;

    /**
     * d选项选择人数
     */
    private int numberD;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public int getQuestionBatchId() {
        return questionBatchId;
    }

    public void setQuestionBatchId(int questionBatchId) {
        this.questionBatchId = questionBatchId;
    }

    public String getQuestionA() {
        return questionA;
    }

    public void setQuestionA(String questionA) {
        this.questionA = questionA;
    }

    public String getQuestionB() {
        return questionB;
    }

    public void setQuestionB(String questionB) {
        this.questionB = questionB;
    }

    public String getQuestionC() {
        return questionC;
    }

    public void setQuestionC(String questionC) {
        this.questionC = questionC;
    }

    public String getQuestionD() {
        return questionD;
    }

    public void setQuestionD(String questionD) {
        this.questionD = questionD;
    }

    public int getNumberA() {
        return numberA;
    }

    public void setNumberA(int numberA) {
        this.numberA = numberA;
    }

    public int getNumberB() {
        return numberB;
    }

    public void setNumberB(int numberB) {
        this.numberB = numberB;
    }

    public int getNumberC() {
        return numberC;
    }

    public void setNumberC(int numberC) {
        this.numberC = numberC;
    }

    public int getNumberD() {
        return numberD;
    }

    public void setNumberD(int numberD) {
        this.numberD = numberD;
    }

    @Override
    public String toString() {
        return "VoteQuestion{" +
                "questionId='" + questionId + '\'' +
                ", questionName='" + questionName + '\'' +
                ", questionBatchId=" + questionBatchId +
                ", questionA='" + questionA + '\'' +
                ", questionB='" + questionB + '\'' +
                ", questionC='" + questionC + '\'' +
                ", questionD='" + questionD + '\'' +
                ", numberA=" + numberA +
                ", numberB=" + numberB +
                ", numberC=" + numberC +
                ", numberD=" + numberD +
                '}';
    }
}

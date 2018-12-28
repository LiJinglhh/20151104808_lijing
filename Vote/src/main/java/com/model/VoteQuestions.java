package com.model;

import java.io.Serializable;
import java.util.Arrays;

public class VoteQuestions implements Serializable {

    /**
     * 问题内容数组
     */
    private String[] questionNames;

    /**
     * 问题1
     */
    private String question1;

    /**
     * 问题2
     */
    private String question2;

    /**
     * 问题3
     */
    private String question3;

    /**
     * 问题4
     */
    private String question4;

    /**
     * 问题5
     */
    private String question5;

    /**
     * a选项内容数组
     */
    private String[] questionAs;

    /**
     * b选项内容数组
     */
    private String[] questionBs;

    /**
     * c选项内容数组
     */
    private String[] questionCs;

    /**
     * d选项内容数组
     */
    private String[] questionDs;

    public String[] getQuestionNames() {
        return questionNames;
    }

    public void setQuestionNames(String[] questionNames) {
        this.questionNames = questionNames;
    }

    public String[] getQuestionAs() {
        return questionAs;
    }

    public void setQuestionAs(String[] questionAs) {
        this.questionAs = questionAs;
    }

    public String[] getQuestionBs() {
        return questionBs;
    }

    public void setQuestionBs(String[] questionBs) {
        this.questionBs = questionBs;
    }

    public String[] getQuestionCs() {
        return questionCs;
    }

    public void setQuestionCs(String[] questionCs) {
        this.questionCs = questionCs;
    }

    public String[] getQuestionDs() {
        return questionDs;
    }

    public void setQuestionDs(String[] questionDs) {
        this.questionDs = questionDs;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getQuestion5() {
        return question5;
    }

    public void setQuestion5(String question5) {
        this.question5 = question5;
    }

    @Override
    public String toString() {
        return "VoteQuestions{" +
                "questionNames=" + Arrays.toString(questionNames) +
                ", question1='" + question1 + '\'' +
                ", question2='" + question2 + '\'' +
                ", question3='" + question3 + '\'' +
                ", question4='" + question4 + '\'' +
                ", question5='" + question5 + '\'' +
                ", questionAs=" + Arrays.toString(questionAs) +
                ", questionBs=" + Arrays.toString(questionBs) +
                ", questionCs=" + Arrays.toString(questionCs) +
                ", questionDs=" + Arrays.toString(questionDs) +
                '}';
    }
}

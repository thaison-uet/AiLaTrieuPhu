package com.thaison.btvn_buoi18_ailatrieuphu;

/**
 * Created by Shin on 9/7/2016.
 */
public class Question {
    private int level;
    private String question;
    private String caseA;
    private String caseB;
    private String caseC;
    private String caseD;
    private int trueCase;

    public Question(int level, String question, String caseA, String caseB, String caseC, String caseD, int trueCase) {
        this.level = level;
        this.question = question;
        this.caseA = caseA;
        this.caseB = caseB;
        this.caseC = caseC;
        this.caseD = caseD;
        this.trueCase = trueCase;
    }

    public int getLevel() {
        return level;
    }

    public String getQuestion() {
        return question;
    }

    public String getCaseA() {
        return caseA;
    }

    public String getCaseB() {
        return caseB;
    }

    public String getCaseC() {
        return caseC;
    }

    public String getCaseD() {
        return caseD;
    }

    public int getTrueCase() {
        return trueCase;
    }
}

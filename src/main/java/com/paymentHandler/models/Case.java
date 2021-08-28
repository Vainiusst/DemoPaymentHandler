package com.paymentHandler.models;

import com.paymentHandler.enums.CaseResolution;
import com.paymentHandler.enums.CaseType;
import java.util.Random;

public class Case {
    public final int caseId;
    public final int paymentId;
    public final CaseType caseType;
    public CaseResolution caseResolution;
    private Random rand;

    public Case(int caseId, int paymentId){
        this.rand = new Random();
        this.caseId = caseId;
        this.paymentId = paymentId;
        this.caseType = CaseType.values()[rand.nextInt(CaseType.values().length)];
        this.caseResolution = CaseResolution.UNRESOLVED;
    }

    public void setCaseResolution(CaseResolution res) { this.caseResolution = res; }

    public int getCaseId() { return this.caseId; }
}

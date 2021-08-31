package com.paymentHandler.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentHandler.enums.CaseResolution;
import com.paymentHandler.enums.CaseType;
import java.util.Random;

public class Case {
    public final int caseId;
    public final int paymentId;
    public final CaseType caseType;
    public CaseResolution caseResolution;
    private final Random rand;

    public Case(int caseId, int paymentId){
        this.rand = new Random();
        this.caseId = caseId;
        this.paymentId = paymentId;
        this.caseType = CaseType.values()[rand.nextInt(CaseType.values().length)];
        this.caseResolution = CaseResolution.UNRESOLVED;
    }

    public int getCaseId() { return this.caseId; }

    public String toJson() {
        String caseJson = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            caseJson = objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            caseJson = String.format("Error while retrieving the case.\n%s", e.getMessage());
        }
        return caseJson;
    }

    public String resolve(CaseResolution resolution, CaseCollection unresolved, CaseCollection resolved) {
        if (this.caseResolution != CaseResolution.UNRESOLVED) return "Case has already been resolved";

        this.caseResolution = resolution;
        resolved.addToCollection(this);
        unresolved.removeFromCollection(this);
        if (resolution == CaseResolution.REJECTED) return String.format("Case %s was rejected.", this.caseId);
        if (resolution == CaseResolution.ACCEPTED) return String.format("Case %s was accepted.", this.caseId);
        return "No action was taken";
    }
}

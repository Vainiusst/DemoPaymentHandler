package com.paymentHandler.models;

import com.paymentHandler.enums.CaseType;
import java.util.ArrayList;
import java.util.List;

public class CaseCollection {
    private final List<Case> cases;

    public CaseCollection() { this.cases = new ArrayList<>(); }

    private CaseCollection(List<Case> caseList){ this.cases = caseList; }

    public int size() { return this.cases.size(); }

    public void addToCollection(Case c) { this.cases.add(c); }

    public CaseCollection addCollection(CaseCollection cc){
        this.cases.addAll(cc.cases);
        return this;
    }

    public Case findCaseById(int caseId) {
        return this.cases.stream().filter(c -> c.caseId == caseId).findFirst().orElse(null);
    }

    public CaseCollection findCasesByCountry(CaseType caseType) {
        return new CaseCollection(this.cases.stream().filter(c -> c.caseType == caseType).toList());
    }

    public List<Integer> getCaseIds() { return this.cases.stream().map(Case::getCaseId).toList(); }

    public void removeFromCollection(Case c) { this.cases.remove(c); }

    public String getInfo() {
        int norwayCases = this.findCasesByCountry(CaseType.NORWAY).size();
        int denmarkCases = this.findCasesByCountry(CaseType.DENMARK).size();
        int swedenCases = this.findCasesByCountry(CaseType.SWEDEN).size();
        int finlandCases = this.findCasesByCountry(CaseType.FINLAND).size();
        List<Integer> unresolvedIds = this.getCaseIds();

        StringBuilder sb = new StringBuilder(String.format("{\"UnresolvedCases\": \"%s\",", this.size()));
        sb.append(String.format("\"NorwayCases\": \"%s\",", norwayCases));
        sb.append(String.format("\"DenmarkCases\": \"%s\",", denmarkCases));
        sb.append(String.format("\"SwedenCases\": \"%s\",", swedenCases));
        sb.append(String.format("\"FinlandCases\": \"%s\",", finlandCases));
        sb.append("\"UnresolvedIds\": ");
        if (unresolvedIds.size() > 0) {
            String unresolvedString = unresolvedIds.toString();
            sb.append(String.format("\"%s\",", unresolvedString));
        }
        else {
            sb.append("\"none\",");
        }
        return sb.toString();
    }
}

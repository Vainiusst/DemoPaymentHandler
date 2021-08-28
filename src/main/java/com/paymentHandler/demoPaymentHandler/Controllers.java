package com.paymentHandler.demoPaymentHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentHandler.enums.CaseResolution;
import com.paymentHandler.enums.CaseType;
import com.paymentHandler.models.Case;
import com.paymentHandler.models.Payment;
import com.paymentHandler.services.Generators;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Controllers {
    private List<Case> unresolvedCases = new ArrayList<>();
    private final Generators generators = new Generators();
    private final List<Case> resolvedCases = new ArrayList<>();
    private final List<Payment> assignedPayments = generators.getAssignedPayments();
    private final ObjectMapper objMapper = new ObjectMapper();

    @RequestMapping("/")
    public String index() {
        StringBuilder sb = new StringBuilder("{\"/getGeneralInfo\": \"find general info about cases\",");
        sb.append("\"/createCases/{num}\": \"create randomized number of cases.");
        sb.append(" {num} specifies the number of cases you want to create\",");
        sb.append("\"/getCase/{caseId}\": \"gets information related to the case and the associated payment.");
        sb.append(" {caseId} specifies the id of the case\",");
        sb.append("\"/getPayment/{paymentId}\": \"gets information related to the case and the associated payment.");
        sb.append(" {paymentId} specifies the id of the payment\",");
        sb.append("\"/rejectCase/{caseId}\": \"rejects the case.");
        sb.append(" {caseId} specifies the id of the case\",");
        sb.append("\"/acceptCase/{caseId}\": \"accepts the case.");
        sb.append(" {caseId} specifies the id of the case\"}");
        return sb.toString();
    }

    @RequestMapping("getGeneralInfo")
    public String getGeneralInfo() {
        int norwayCases = unresolvedCases.stream().filter(c -> c.caseType == CaseType.NORWAY).toList().size();
        int denmarkCases = unresolvedCases.stream().filter(c -> c.caseType == CaseType.DENMARK).toList().size();
        int swedenCases = unresolvedCases.stream().filter(c -> c.caseType == CaseType.SWEDEN).toList().size();
        int finlandCases = unresolvedCases.stream().filter(c -> c.caseType == CaseType.FINLAND).toList().size();
        List<Integer> unresolvedIds = unresolvedCases.stream().map(Case::getCaseId).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder(String.format("{\"UnresolvedCases\": \"%s\",", unresolvedCases.size()));
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
        sb.append(String.format("\"ResolvedCases\": \"%s\"}", resolvedCases.size()));
        return sb.toString();
    }

    @RequestMapping("createCases/{qty}")
    public String createCases(@PathVariable("qty") int qty) {
        int casesBefore = unresolvedCases.size();
        try {
            generators.generatePayments(qty);
            unresolvedCases = generators.generateCases();
        } catch (Exception e) {
            return String.format("{\"message\": \"Generators have failed\", \"error\": \"%s\"", e.getMessage());
        }
        int casesAfter = unresolvedCases.size();
        int diff = casesAfter - casesBefore;
        String message = String.format("%s cases were created, out of %s attempted", diff, qty);
        return jsonifyString(message);
    }

    @RequestMapping("getCase/{caseId}")
    public String getCase(@PathVariable("caseId") int caseId){
        Case myCase = unresolvedCases.stream().filter(c -> c.caseId == caseId).findFirst().orElse(null);
        if (myCase == null) return jsonifyString("No such case found");
        String caseJson = "";
        try {
            caseJson = objMapper.writeValueAsString(myCase);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return caseJson;
    }

    @RequestMapping("getPayment/{paymentId}")
    public String getPayment(@PathVariable("paymentId") int paymentId) {
        Payment myPayment = assignedPayments.stream()
                .filter(c -> c.paymentId == paymentId).findFirst().orElse(null);
        if (myPayment == null) return jsonifyString("No such payment found");
        String paymentJson = "";
        try {
            paymentJson = objMapper.writeValueAsString(myPayment);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return paymentJson;
    }

    @RequestMapping("rejectCase/{caseId}")
    public String rejectCase(@PathVariable("caseId") int caseId) {
        Case myCase = unresolvedCases.stream()
                .filter(c -> c.caseId == caseId).findFirst().orElse(null);
        if (myCase == null) return "No such case found.";

        myCase.setCaseResolution(CaseResolution.REJECTED);
        resolvedCases.add(myCase);
        unresolvedCases.remove(myCase);
        String message = String.format("Case %s was rejected.", caseId);
        return jsonifyString(message);
    }

    @RequestMapping("acceptCase/{caseId}")
    public String acceptCase(@PathVariable("caseId") int caseId) {
        Case myCase = unresolvedCases.stream().filter(c -> c.caseId == caseId).findFirst().orElse(null);
        if (myCase == null) return "No such case found.";

        myCase.setCaseResolution(CaseResolution.ACCEPTED);
        resolvedCases.add(myCase);
        unresolvedCases.remove(myCase);
        String message = String.format("Case %s was accepted.", caseId);
        return jsonifyString(message);
    }

    private String jsonifyString(String message) { return String.format("{\"message\": \"%s\"}", message); }

    public int getNumberOfRoutes() {
        int result = 0;
        for (Method m : Controllers.class.getMethods()) {
            if (m.isAnnotationPresent(RequestMapping.class)) result++;
        }
        return result;
    }
}

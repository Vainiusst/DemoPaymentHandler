package com.paymentHandler.demoPaymentHandler;

import com.paymentHandler.enums.CaseResolution;
import com.paymentHandler.models.Case;
import com.paymentHandler.models.CaseCollection;
import com.paymentHandler.models.Payment;
import com.paymentHandler.models.PaymentCollection;
import com.paymentHandler.services.Generators;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.reflect.Method;

@RestController
public class Controllers {
    private CaseCollection unresolvedCases = new CaseCollection();
    private final CaseCollection resolvedCases = new CaseCollection();
    private final Generators generators = new Generators();
    private final PaymentCollection assignedPayments = generators.getAssignedPayments();

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
        StringBuilder sb = new StringBuilder(unresolvedCases.getInfo());
        sb.append(String.format("\"ResolvedCases\": \"%s\"}", resolvedCases.size()));
        return sb.toString();
    }

    @RequestMapping("createCases/{qty}")
    public String createCases(@PathVariable("qty") int qty) {
        int casesBefore = unresolvedCases.size();
        try {
            unresolvedCases = generators.newCases(qty);
        } catch (Exception e) {
            return String.format("{\"message\": \"Generators have failed\", \"error\": \"%s\"", e.getMessage());
        }
        int diff = unresolvedCases.size() - casesBefore;
        return jsonifyString(String.format("%s cases were created, out of %s attempted", diff, qty));
    }

    @RequestMapping("getCase/{caseId}")
    public String getCase(@PathVariable("caseId") int caseId){
        Case myCase = unresolvedCases.findCaseById(caseId);
        if (myCase == null) return jsonifyString("No such case found");
        return myCase.toJson();
    }

    @RequestMapping("getPayment/{paymentId}")
    public String getPayment(@PathVariable("paymentId") int paymentId) {
        Payment myPayment = assignedPayments.findPaymentByPaymentId(paymentId);
        if (myPayment == null) return jsonifyString("No such payment found");
        return myPayment.toJson();
    }

    @RequestMapping("rejectCase/{caseId}")
    public String rejectCase(@PathVariable("caseId") int caseId) {
        Case myCase = unresolvedCases.findCaseById(caseId);
        if (myCase == null) return jsonifyString("No such case found.");
        return jsonifyString(myCase.resolve(CaseResolution.REJECTED, unresolvedCases, resolvedCases));
    }

    @RequestMapping("acceptCase/{caseId}")
    public String acceptCase(@PathVariable("caseId") int caseId) {
        Case myCase = unresolvedCases.findCaseById(caseId);
        if (myCase == null) return jsonifyString("No such case found.");
        return jsonifyString(myCase.resolve(CaseResolution.ACCEPTED, unresolvedCases, resolvedCases));
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

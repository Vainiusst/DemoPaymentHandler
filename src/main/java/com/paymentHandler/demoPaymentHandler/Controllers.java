package com.paymentHandler.demoPaymentHandler;

import com.paymentHandler.enums.CaseResolution;
import com.paymentHandler.models.Case;
import com.paymentHandler.models.CaseCollection;
import com.paymentHandler.models.Payment;
import com.paymentHandler.models.PaymentCollection;
import com.paymentHandler.services.Generators;
import com.paymentHandler.services.Helpers;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controllers {
    private CaseCollection unresolvedCases = new CaseCollection();
    private final CaseCollection resolvedCases = new CaseCollection();
    private final Generators generators = new Generators();
    private final PaymentCollection assignedPayments = generators.getAssignedPayments();
    private final Helpers helpers = new Helpers();

    @RequestMapping(value = "/", method = RequestMethod.GET)
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

    @RequestMapping(value = "generalInfo", method = RequestMethod.GET)
    public String getGeneralInfo() {
        StringBuilder sb = new StringBuilder(unresolvedCases.getInfo());
        sb.append(String.format("\"ResolvedCases\": \"%s\"}", resolvedCases.size()));
        return sb.toString();
    }

    @RequestMapping(value = "createCases/{qty}", method = RequestMethod.POST)
    public String createCases(@PathVariable("qty") int qty) {
        int casesBefore = unresolvedCases.size();
        unresolvedCases.addCollection(generators.newCases(qty));
        int diff = unresolvedCases.size() - casesBefore;
        return helpers.jsonifyString(String.format("%s cases were created, out of %s attempted", diff, qty));
    }

    @RequestMapping(value = "cases/{caseId}", method = RequestMethod.GET)
    public String getCase(@PathVariable("caseId") int caseId){
        Case myCase = unresolvedCases.findCaseById(caseId);
        if (myCase == null) myCase = resolvedCases.findCaseById(caseId);
        if (myCase == null) return helpers.jsonifyString("No such payment found");
        return myCase.toJson();
    }

    @RequestMapping(value = "payments/{paymentId}", method = RequestMethod.GET)
    public String getPayment(@PathVariable("paymentId") int paymentId) {
        Payment myPayment = assignedPayments.findPaymentByPaymentId(paymentId);
        if (myPayment == null) return helpers.jsonifyString("No such payment found");
        return myPayment.toJson();
    }

    @RequestMapping(value = "rejectCase/{caseId}", method = RequestMethod.PUT)
    public String rejectCase(@PathVariable("caseId") int caseId) {
        Case myCase = unresolvedCases.findCaseById(caseId);
        if (myCase == null) return helpers.jsonifyString("No such case found.");
        return helpers.jsonifyString(myCase.resolve(CaseResolution.REJECTED, unresolvedCases, resolvedCases));
    }

    @RequestMapping(value = "acceptCase/{caseId}", method = RequestMethod.PUT)
    public String acceptCase(@PathVariable("caseId") int caseId) {
        Case myCase = unresolvedCases.findCaseById(caseId);
        if (myCase == null) return helpers.jsonifyString("No such case found.");
        return helpers.jsonifyString(myCase.resolve(CaseResolution.ACCEPTED, unresolvedCases, resolvedCases));
    }
}

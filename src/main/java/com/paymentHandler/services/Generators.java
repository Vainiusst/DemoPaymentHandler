package com.paymentHandler.services;

import com.paymentHandler.models.Case;
import com.paymentHandler.models.CaseCollection;
import com.paymentHandler.models.Payment;
import com.paymentHandler.models.PaymentCollection;
import java.util.Random;

public class Generators {
    private final PaymentCollection newPayments;
    private final PaymentCollection assignedPayments;
    private int storedPaymentId;
    private int storedCaseId;
    private final Random rand;

    public Generators () {
        this.newPayments = new PaymentCollection();
        this.assignedPayments = new PaymentCollection();
        this.storedPaymentId = 1;
        this.storedCaseId = 1;
        this.rand = new Random();
    }

    public CaseCollection newCases(int qty) {
        generatePayments(qty);
        return generateCases();
    }

    public PaymentCollection getAssignedPayments() { return this.assignedPayments; }

    private void generatePayments(int qty) {
        for (int i = 0; i < qty; i++) {
            newPayments.addToCollection(new Payment(storedPaymentId));
            storedPaymentId++;
        }
    }

    private CaseCollection generateCases() {
        CaseCollection unresolvedCases = new CaseCollection();
        while (newPayments.size() > 0) {
            Payment randPayment = newPayments.removeFromCollection(rand.nextInt(newPayments.size()));
            unresolvedCases.addToCollection(new Case(storedCaseId, randPayment.paymentId));
            storedCaseId++;
            assignedPayments.addToCollection(randPayment);
        }
        return unresolvedCases;
    }
}

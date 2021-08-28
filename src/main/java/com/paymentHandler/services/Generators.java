package com.paymentHandler.services;

import com.paymentHandler.models.Case;
import com.paymentHandler.models.Payment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generators {
    private final List<Payment> newPayments;
    private final List<Payment> assignedPayments;
    private int storedPaymentId;
    private int storedCaseId;
    private final Random rand;

    public Generators () {
        this.newPayments = new ArrayList<>();
        this.assignedPayments = new ArrayList<>();
        this.storedPaymentId = 1;
        this.storedCaseId = 1;
        this.rand = new Random();
    }

    public List<Payment> getAssignedPayments() {
        return this.assignedPayments;
    }

    public void generatePayments(int qty) {
        for (int i = 0; i < qty; i++) {
            newPayments.add(new Payment(storedPaymentId));
            storedPaymentId++;
        }
    }

    public List<Case> generateCases() {
        List<Case> unresolvedCases = new ArrayList<>();
        while (newPayments.size() > 0) {
            Payment randPayment = newPayments.remove(rand.nextInt(newPayments.size()));
            unresolvedCases.add(new Case(storedCaseId, randPayment.paymentId));
            storedCaseId++;
            assignedPayments.add(randPayment);
        }
        return unresolvedCases;
    }
}

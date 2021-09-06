package com.paymentHandler.models;

import java.util.ArrayList;
import java.util.List;

public class PaymentCollection {
    List<Payment> payments;

    public PaymentCollection() { this.payments = new ArrayList<>(); }

    public void addToCollection(Payment p) { this.payments.add(p); }

    public Payment removeFromCollection(int index) { return this.payments.remove(index); }

    public int size() { return this.payments.size(); }

    public Payment findPaymentByPaymentId(int paymentId) {
        return this.payments.stream().filter(c -> c.paymentId == paymentId).findFirst().orElse(null);
    }
}

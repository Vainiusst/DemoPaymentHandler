package com.paymentHandler.models;

import com.paymentHandler.enums.Currency;
import java.math.BigDecimal;
import java.util.Random;

public class Payment {
    public final int paymentId;
    public final BigDecimal amount;
    public final Currency currency;
    private Random rand;

    public Payment(int paymentId) {
        this.rand = new Random();
        this.paymentId = paymentId;
        this.amount = new BigDecimal(String.format("%.4f", rand.nextInt(9999) + rand.nextDouble()));
        this.currency = Currency.values()[rand.nextInt(Currency.values().length)];
    }
}


package com.paymentHandler.models;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public String toJson() {
        String pmntJson = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            pmntJson = objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            pmntJson = String.format("Error while retrieving the payment.\n%s", e.getMessage());
        }
        return pmntJson;
    }
}


package com.paymentHandler.services;

import com.paymentHandler.demoPaymentHandler.Controllers;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

public class Helpers {
    public String jsonifyString(String message) { return String.format("{\"message\": \"%s\"}", message); }

    public int getNumberOfRoutes() {
        int result = 0;
        for (Method m : Controllers.class.getMethods()) {
            if (m.isAnnotationPresent(RequestMapping.class)) result++;
        }
        return result;
    }
}

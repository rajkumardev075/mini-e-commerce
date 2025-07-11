package com.payment_service.payment_service.threadLocal;

import java.util.UUID;

public class CorrelationIdHolder {
    public static final ThreadLocal<String> correlationIdHolder = new ThreadLocal<>();

    public static void setCorrelationId(String correlationId) {
        correlationIdHolder.set(correlationId);
    }

    public static String getCorrelationId() {
        return correlationIdHolder.get();
    }

    public static void clear() {
        correlationIdHolder.remove();
    }

    public static String getOrGenerate() {
        String id = getCorrelationId();
        if (id == null) {
            id = UUID.randomUUID().toString();
            setCorrelationId(id);
        }
        return id;
    }
}

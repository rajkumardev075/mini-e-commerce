package com.order_service.order_service.threadLocalStore;

public class CorrelationIdHolder {
    private static final ThreadLocal<String> correlationId = new ThreadLocal<>();

    public static void set(String id) {
        correlationId.set(id);
    }

    public static String get() {
        return correlationId.get();
    }

    public static void clear() {
        correlationId.remove();
    }
}

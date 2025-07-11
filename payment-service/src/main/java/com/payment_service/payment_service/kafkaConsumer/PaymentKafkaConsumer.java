package com.payment_service.payment_service.kafkaConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment_service.payment_service.DTO.OrderCreatedEvent;
import com.payment_service.payment_service.constants.ApplicationConstants;
import com.payment_service.payment_service.services.PaymentService;
import com.payment_service.payment_service.threadLocal.CorrelationIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentKafkaConsumer {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = ApplicationConstants.ORDER_CREATED_EVENT, groupId = "payment-service")
    public void consume(byte[] message, @Header("X-Correlation-Id") String correlationId) {
        CorrelationIdHolder.setCorrelationId(correlationId);
        MDC.put("correlationId", correlationId);

        try {
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);
            log.info("Received order-created event: {}", event);
            paymentService.processPayment(event);
        } catch (Exception e) {
            log.error("Failed to deserialize OrderCreatedEvent", e);
        } finally {
            CorrelationIdHolder.clear();
            MDC.clear();
        }
    }
}

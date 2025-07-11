package com.order_service.order_service.kafkaConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_service.order_service.DTO.PaymentCompletedEvent;
import com.order_service.order_service.constant.ApplicationConstants;
import com.order_service.order_service.services.OrderService;
import com.order_service.order_service.threadLocalStore.CorrelationIdHolder;
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

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = ApplicationConstants.PAYMENT_COMPLETED_EVENT_TOPIC, groupId = "order-service")
    public void consume(byte[] message, @Header("X-Correlation-Id") String correlationId) {
        CorrelationIdHolder.set(correlationId);
        MDC.put("correlationId", correlationId);

        try {
            PaymentCompletedEvent event = objectMapper.readValue(message, PaymentCompletedEvent.class);
            log.info("Received order-Success event: {}", event);
            orderService.updateOrderStatus(Long.parseLong(event.getOrderId()), "COMPLETED");
        } catch (Exception e) {
            log.error("Failed to deserialize OrderSuccessEvent", e);
        } finally {
            CorrelationIdHolder.clear();
            MDC.clear();
        }
    }
}

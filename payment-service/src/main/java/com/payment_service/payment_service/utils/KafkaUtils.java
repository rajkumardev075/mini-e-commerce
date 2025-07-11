package com.payment_service.payment_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment_service.payment_service.DTO.PaymentCompletedEvent;
import com.payment_service.payment_service.threadLocal.CorrelationIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.payment_service.payment_service.constants.ApplicationConstants.PAYMENT_COMPLETED_EVENT_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaUtils {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendPaymentCompletedEvent(PaymentCompletedEvent payment) {
        try {
            log.info("Sending Order Created Event to Kafka {}", payment.getOrderId());

            byte[] payload = objectMapper.writeValueAsBytes(payment);

            ProducerRecord<String, byte[]> record =
                    new ProducerRecord<>(PAYMENT_COMPLETED_EVENT_TOPIC, payload);

            record.headers().add("X-Correlation-Id", CorrelationIdHolder.getCorrelationId().getBytes());

            kafkaTemplate.send(record);

            log.info("Sent Order Created Event to Kafka {}", payload);
        } catch (Exception e) {
            log.error("Failed to send OrderCreatedEvent", e);
        }
    }
}

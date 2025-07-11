package com.order_service.order_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_service.order_service.DTO.OrderCreatedEvent;
import com.order_service.order_service.constant.ApplicationConstants;
import com.order_service.order_service.threadLocalStore.CorrelationIdHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaUtils {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendOrderCreatedEvent(OrderCreatedEvent order) {
        try {
            log.info("Sending Order Created Event to Kafka {}", order.getOrderId());

            byte[] payload = objectMapper.writeValueAsBytes(order);

            ProducerRecord<String, byte[]> record =
                    new ProducerRecord<>(ApplicationConstants.ORDER_CREATED_EVENT, payload);

            record.headers().add("X-Correlation-Id", CorrelationIdHolder.get().getBytes());

            kafkaTemplate.send(record);

            log.info("Sent Order Created Event to Kafka {}", payload);
        } catch (Exception e) {
            log.error("Failed to send OrderCreatedEvent", e);
        }
    }
}

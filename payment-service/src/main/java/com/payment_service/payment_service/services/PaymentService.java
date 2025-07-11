package com.payment_service.payment_service.services;

import com.payment_service.payment_service.DTO.OrderCreatedEvent;
import com.payment_service.payment_service.DTO.PaymentCompletedEvent;
import com.payment_service.payment_service.entity.PaymentEntity;
import com.payment_service.payment_service.repository.PaymentRepository;
import com.payment_service.payment_service.utils.KafkaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final KafkaUtils kafkaUtils;

    public void processPayment(OrderCreatedEvent event) {
        log.info("Processing payment for order {}", event.getOrderId());

        PaymentEntity paymentSaved = PaymentEntity.builder().orderId(event.getOrderId()).amount(event.getAmount()).status("SUCCESS").paidAt(LocalDateTime.now()).build();
        paymentRepository.save(paymentSaved);

        PaymentCompletedEvent completedEvent = PaymentCompletedEvent.builder().orderId(paymentSaved.getOrderId()).paymentStatus(paymentSaved.getStatus()).build();
        kafkaUtils.sendPaymentCompletedEvent(completedEvent);
        log.info("Published payment-completed event for order {}", event.getOrderId());
    }
}

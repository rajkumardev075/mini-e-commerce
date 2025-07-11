package com.order_service.order_service.services;

import com.order_service.order_service.DTO.OrderCreatedEvent;
import com.order_service.order_service.DTO.OrderRequestDTO;
import com.order_service.order_service.DTO.OrderResponseDTO;
import com.order_service.order_service.mapper.OrderMapper;
import com.order_service.order_service.model.OrderEntity;
import com.order_service.order_service.repository.OrderRepository;
import com.order_service.order_service.utils.KafkaUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaUtils kafkaUtils;


    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
       OrderEntity savedOrder = orderRepository.save(OrderMapper.toEntity(orderRequestDTO));
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder().orderId(String.valueOf(savedOrder.getId())).amount(Double.valueOf(savedOrder.getQuantity())).build();
        kafkaUtils.sendOrderCreatedEvent(orderCreatedEvent);
       return OrderMapper.toResponseDTO(savedOrder);
    }

    public void updateOrderStatus(Long orderId, String newStatus) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            order.setStatus(newStatus);
            orderRepository.save(order);
        } else {
            throw new EntityNotFoundException("Order with id " + orderId + " not found");
        }
    }
}

package com.order_service.order_service.mapper;

import com.order_service.order_service.DTO.OrderRequestDTO;
import com.order_service.order_service.DTO.OrderResponseDTO;
import com.order_service.order_service.model.OrderEntity;

import java.time.LocalDateTime;

public class OrderMapper {
    public static OrderEntity toEntity(OrderRequestDTO order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setProductId(order.getProductId());
        orderEntity.setQuantity(order.getQuantity());
        orderEntity.setStatus("PENDING");
        orderEntity.setCreatedAt(LocalDateTime.now());
        return orderEntity;
    }
    public static OrderResponseDTO toResponseDTO(OrderEntity entity) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(entity.getId());
        responseDTO.setStatus(entity.getStatus());
        return responseDTO;
    }
}

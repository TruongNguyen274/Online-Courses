package com.online.learning.model.mapper;

import com.online.learning.model.dto.OrderDTO;
import com.online.learning.model.entity.Order;

import java.util.List;

public interface OrderMapper {
    OrderDTO toDTO(Order order);

    List<OrderDTO> toListDTO(List<Order> orderList);

    Order toEntity(OrderDTO orderDTO);
}

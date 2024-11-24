package com.example.ecommerce_app.Services.OrderItem;

import com.example.ecommerce_app.Dto.OrderItem.OrderItemResponseDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemStatusDto;
import com.example.ecommerce_app.Entity.OrderItem;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.OrderItem.OrderItemRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class OrderItemServiceImp implements OrderItemService{

    private final OrderItemRepository orderItemRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<OrderItemResponseDto> getOrderItems(long orderId) {

        List<OrderItem> orderItems = orderItemRepository.findAllById(Collections.singleton(orderId));

        List<OrderItemResponseDto> orderItemResponseDtos = new ArrayList<>(orderItems.size());

        for(OrderItem orderItem : orderItems)  orderItemResponseDtos
                .add(OrderItemResponseDto
                        .builder()
                        .price(orderItem.getPrice())
                        .productName(orderItem.getProduct().getName())
                        .totalAmount(orderItem.getPrice() * orderItem.getQuantity())
                        .quantity(orderItem.getQuantity())
                        .productThumbnail(orderItem.getProduct().getThumbNail())
                        .build());

        return orderItemResponseDtos;

    }

    @Transactional
    @Override
    public void removeAllOrderItems(long orderId) {
        try {
            orderItemRepository.deleteAllInBatch();
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to remove order items ");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderItemResponseDto> getOrderItemsByVendorId(long vendorId , Pageable pageable){
        Page<OrderItem> orderItemList = orderItemRepository.findByVendorId(vendorId , pageable);
        List<OrderItemResponseDto> orderItemResponseDtos = new ArrayList<>(orderItemList.getNumberOfElements());

        for(OrderItem orderItem : orderItemList)  orderItemResponseDtos
                .add(OrderItemResponseDto
                        .builder()
                        .price(orderItem.getPrice())
                        .productName(orderItem.getProduct().getName())
                        .totalAmount(orderItem.getPrice() * orderItem.getQuantity())
                        .quantity(orderItem.getQuantity())
                        .productThumbnail(orderItem.getProduct().getThumbNail())
                        .build());
        return new PageImpl<>(orderItemResponseDtos);
    }




}

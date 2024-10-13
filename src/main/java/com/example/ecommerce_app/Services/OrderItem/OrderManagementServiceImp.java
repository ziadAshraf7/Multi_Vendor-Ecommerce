package com.example.ecommerce_app.Services.OrderItem;

import com.example.ecommerce_app.Dto.OrderItem.OrderItemCreationDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemRemovalDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemResponseDto;
import com.example.ecommerce_app.Entity.Embedded_Ids.OrderItemEmbeddedId;
import com.example.ecommerce_app.Entity.Order;
import com.example.ecommerce_app.Entity.OrderItem;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotFoundException;
import com.example.ecommerce_app.Repositery.Order.OrderRepository;
import com.example.ecommerce_app.Repositery.OrderItem.OrderItemRepository;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class OrderManagementServiceImp implements OrderManagementService {

    private final OrderItemRepository orderItemRepository;

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addOrderItem(OrderItemCreationDto orderItemCreationDto) {

        try {
            Order order = orderRepository.getReferenceById(orderItemCreationDto.getOrderId());

            Product product = productRepository.getReferenceById(orderItemCreationDto.getProductId());

            User customer = userRepository.getReferenceById(orderItemCreationDto.getCustomerId());

            OrderItem orderItem = OrderItem
                    .builder()
                    .quantity(orderItemCreationDto.getQuantity())
                    .price(orderItemCreationDto.getPrice())
                    .customer(customer)
                    .order(order)
                    .product(product)
                    .orderItemEmbeddedId(new OrderItemEmbeddedId())
                    .build();

            order.setTotalAmount(order.getTotalAmount() + (orderItem.getPrice() * orderItem.getQuantity()));

            orderItemRepository.save(orderItem);
            orderRepository.save(order);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to add orderItem ");
        }
    }

    @Override
    @Transactional
    public void deleteOrderItem(OrderItemRemovalDto orderItemRemovalDto) {
        try {

            Order order = orderRepository.getReferenceById(orderItemRemovalDto.getOrderId());

            OrderItem orderItem = orderItemRepository.findByOrderIdAndProductId(
                    orderItemRemovalDto.getOrderId(), orderItemRemovalDto.getProductId()
            );

            orderItemRepository.deleteByOrderIdAndProductId(
                    orderItemRemovalDto.getOrderId(),
                    orderItemRemovalDto.getProductId());

            order.setTotalAmount(order.getTotalAmount() - (orderItem.getQuantity() * orderItem.getPrice()));
            orderRepository.save(order);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to delete order item");
        }
    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(long orderId) {
        try {

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
        }catch (RuntimeException e){
             log.error(e.getMessage());
             throw new NotFoundException("Cannot find order items ");
        }
    }

    @Override
    public void removeAllOrderItems(long orderId) {
        try {
            orderItemRepository.deleteAllInBatch();
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to remove order items ");
        }
    }
}

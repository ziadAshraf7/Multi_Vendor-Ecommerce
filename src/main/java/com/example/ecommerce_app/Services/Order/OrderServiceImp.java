package com.example.ecommerce_app.Services.Order;

import com.example.ecommerce_app.Dto.Order.OrderResponseDto;
import com.example.ecommerce_app.Dto.Order.OrderUpdateDto;
import com.example.ecommerce_app.Entity.Enums.OrderStatus;
import com.example.ecommerce_app.Entity.Order;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotFoundException;
import com.example.ecommerce_app.Repositery.Order.OrderRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Services.User.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Data
@Service
@Slf4j
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    @Override
    public void createOrder(long customerId) {

          User customer = userService.getUserEntityById(customerId);

          Order order = Order
                  .builder()
                  .customer(customer)
                  .totalAmount(0.0)
                  .status(OrderStatus.PENDING)
                  .build();

          orderRepository.save(order);

    }

    @Override
    public OrderResponseDto getOrderData(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException("Cannot find the order id " + orderId)
        );

        return OrderResponseDto
                .builder()
                .customerName(order.getCustomer().getUserName())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    @Override
    public void removeOrder(long orderId) {
        try {
            orderRepository.deleteById(orderId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to delete order id " + orderId);
        }
    }

    @Override
    public void updateOrder(OrderUpdateDto orderUpdateDto) {

        Order order = orderRepository.findById(orderUpdateDto.getOrderId())
                .orElseThrow(() -> new NotFoundException("cannot find order  "));

        Double newTotalAmount = orderUpdateDto.getTotalAmount();

        OrderStatus orderStatus = orderUpdateDto.getOrderStatus();

        if(newTotalAmount != null) order.setTotalAmount(newTotalAmount);

        if(orderStatus != null) order.setStatus(orderStatus);

        orderRepository.save(order);
    }
}

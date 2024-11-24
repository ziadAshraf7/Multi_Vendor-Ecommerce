package com.example.ecommerce_app.Services.Order;

import com.example.ecommerce_app.Dto.Order.OrderResponseDto;
import com.example.ecommerce_app.Dto.Order.OrderUpdateDto;
import com.example.ecommerce_app.Entity.Order;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Order.OrderRepository;
import com.example.ecommerce_app.Services.User.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Data
@Service
@Slf4j
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    @Transactional
    @Override
    public void createOrder(long customerId) {

          User customer = userService.getUserEntityById(customerId);

          Order newOrder = Order
                  .builder()
                  .customer(customer)
                  .totalAmount(0.0)
                  .build();
          try {
              orderRepository.save(newOrder);

          }catch (DatabasePersistenceException e){
              log.error(e.getMessage());
              throw new DatabasePersistenceException("Database error while creating order");
          }

    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponseDto getOrderData(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomNotFoundException("Cannot find the order id " + orderId)
        );

        return OrderResponseDto
                .builder()
                .customerName(order.getCustomer().getUserName())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    @Transactional
    @Override
    public void removeOrder(long orderId) {
        try {
            orderRepository.deleteById(orderId);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to delete order id " + orderId);
        }
    }

    @Transactional
    @Override
    public void updateOrder(OrderUpdateDto orderUpdateDto) {

        Order order = orderRepository.findById(orderUpdateDto.getOrderId())
                .orElseThrow(() -> new CustomNotFoundException("cannot find order  "));

        Double newTotalAmount = orderUpdateDto.getTotalAmount();

        if(newTotalAmount != null) order.setTotalAmount(newTotalAmount);

        try {
            orderRepository.save(order);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Database Error while Updating order");
        }
    }
}

package com.example.ecommerce_app.Controller;
import com.example.ecommerce_app.Dto.Order.OrderResponseDto;
import com.example.ecommerce_app.Services.Order.OrderService;
import com.example.ecommerce_app.Services.OrderManagement.OrderManagementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    OrderManagementService orderManagementService;

    OrderService orderService;

    @PostMapping("/makeOrder")
    public ResponseEntity<String> makeOrder(){
        orderManagementService.makeOrder();
        return ResponseEntity.ok("Order has Made Successfully");
    }

    @GetMapping
    public ResponseEntity<OrderResponseDto> getOrderData(@RequestParam("orderId") long orderId){
        OrderResponseDto orderResponseDto = orderService.getOrderData(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }


}

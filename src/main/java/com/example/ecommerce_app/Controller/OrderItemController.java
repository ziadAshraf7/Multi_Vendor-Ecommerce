package com.example.ecommerce_app.Controller;


import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemResponseDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemStatusDto;
import com.example.ecommerce_app.Dto.Paginating.PageDto;
import com.example.ecommerce_app.Services.OrderItem.OrderItemService;
import com.example.ecommerce_app.Services.OrderManagement.OrderManagementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orderItem")
@AllArgsConstructor
public class OrderItemController {

    OrderManagementService orderManagementService;

    OrderItemService orderItemService;

    @PutMapping
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> updateOrderItemStatus(@RequestBody OrderItemStatusDto orderItemStatusDto){
        orderManagementService.changeOrderItemStatus(orderItemStatusDto);
        return ResponseEntity.ok("Updated");
    }

    @GetMapping()
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItem(@RequestParam("orderId") long orderId){
        List<OrderItemResponseDto> orderItemResponseDtoPage = orderItemService.getOrderItems(orderId);
        return ResponseEntity.ok(orderItemResponseDtoPage);
    }

    @GetMapping("/vendor")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Page<OrderItemResponseDto>> getOrderItemsPerVendor(   @RequestParam int pageNumber, @RequestParam int pageSize){
        PageRequest pageRequest = PageRequest.of(pageNumber , pageSize);
        long vendorId = ((AuthenticatedUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(orderItemService.getOrderItemsByVendorId(vendorId , pageRequest));
    }

}

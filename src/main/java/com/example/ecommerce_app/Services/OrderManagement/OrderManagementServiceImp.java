package com.example.ecommerce_app.Services.OrderManagement;

import com.example.ecommerce_app.Dto.OrderItem.OrderItemBatchSavedDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemStatusDto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Entity.Enums.OrderItemStatus;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Order.OrderRepository;
import com.example.ecommerce_app.Repositery.OrderItem.OrderItemRepository;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.CartItem.CartItemService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
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

    private final VendorProductRepository vendorProductRepository;

    private final CartItemService cartItemService;

    @Transactional
    @Override
    public void makeOrder(OrderItemBatchSavedDto orderItemBatchSavedDto) {
        cartItemService.removeAllFromCart(orderItemBatchSavedDto.getCustomerId());

        User customer = userRepository.findById(orderItemBatchSavedDto.getCustomerId())
                .orElseThrow(() -> new CustomNotFoundException("user is not found") );

        Order newOrder = Order.builder().customer(customer).build();


        List<Long> productIdList = new ArrayList<>(orderItemBatchSavedDto.getOrderItemGeneralDtoList().size());
        List<Long> vendorProductsIdList = new ArrayList<>(orderItemBatchSavedDto.getOrderItemGeneralDtoList().size());
        orderItemBatchSavedDto.getOrderItemGeneralDtoList().forEach(orderItemGeneralDto -> {
            productIdList.add(orderItemGeneralDto.getProductId());
            vendorProductsIdList.add(orderItemGeneralDto.getVendorProductId());
        });


        List<Product> products = productRepository.findAllById(productIdList);
        List<VendorProduct> vendorProductList = vendorProductRepository.findAllVendorProductById(vendorProductsIdList);

        products.sort(Comparator.comparing(Product::getId));
        vendorProductList.sort(Comparator.comparing(vendorProduct -> vendorProduct.getProduct().getId()));

        List<OrderItem> orderItemList = new ArrayList<>(products.size());

        for(int i = 0 ; i< products.size() ; i++){
            VendorProduct vendorProduct = vendorProductList.get(i);
            vendorProduct.setStock(vendorProduct.getStock() - 1);
            Product product = products.get(i);
            orderItemList.add(OrderItem.builder()
                            .price(vendorProduct.getPrice())
                            .order(newOrder)
                            .product(product)
                    .build());
            newOrder.setTotalAmount(newOrder.getTotalAmount() + vendorProduct.getPrice());
        }

        try {
            orderRepository.save(newOrder);
            vendorProductRepository.saveAll(vendorProductList);
            orderItemRepository.saveAll(orderItemList);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Error while Creating Order");
        }
    }


    @Transactional
    @Override
    public void changeOrderItemStatus(OrderItemStatusDto orderItemStatusDto ) {

        User customer = userRepository.findById(orderItemStatusDto.getCustomerId())
                .orElseThrow(() -> new CustomNotFoundException("user is not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() != customer.getEmail()){
            throw new CustomBadRequestException("user is not authorized to change order item status");
        }


        OrderItem orderItem = orderItemRepository.findById(orderItemStatusDto.getOrderItemId())
                .orElseThrow(() -> new CustomNotFoundException("order item is not found"));

        orderItem.setStatus(orderItemStatusDto.getOrderItemStatus());

        try {
            if(orderItemStatusDto.getOrderItemStatus() == OrderItemStatus.CANCELLED){
                VendorProduct vendorProduct = vendorProductRepository.findById(
                        orderItemStatusDto.getVendorProductId()
                ).orElseThrow(() -> new CustomNotFoundException("vendor product is not found"));

                vendorProduct.setStock(vendorProduct.getStock() + 1);
                vendorProductRepository.save(vendorProduct);
            }

            orderItemRepository.save(orderItem);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Database error while updating order item status");
        }
    }


}

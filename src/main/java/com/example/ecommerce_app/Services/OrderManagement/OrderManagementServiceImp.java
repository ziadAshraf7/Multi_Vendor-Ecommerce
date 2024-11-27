package com.example.ecommerce_app.Services.OrderManagement;

import com.example.ecommerce_app.Dto.OrderItem.OrderItemBatchSavedDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemStatusDto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Entity.Enums.OrderItemStatus;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
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

        User customer = userRepository.findById(orderItemBatchSavedDto.getCustomerId())
                .orElseThrow(() -> new CustomNotFoundException("user is not found") );

        long cartId = customer.getCart().get(0).getId();

        cartItemService.removeAllFromCart(cartId);

        Order newOrder = Order.builder().customer(customer).build();


        List<Long> vendorProductsIdList = new ArrayList<>(orderItemBatchSavedDto.getOrderItemGeneralDtoList().size());
        List<Integer> quantities = new ArrayList<>();
        orderItemBatchSavedDto.getOrderItemGeneralDtoList().forEach(orderItemGeneralDto -> {
            quantities.add(orderItemGeneralDto.getQuantity());
            vendorProductsIdList.add(orderItemGeneralDto.getVendorProductId());
        });


        List<VendorProduct> vendorProductList = vendorProductRepository.findAllVendorProductById(vendorProductsIdList);


        List<OrderItem> orderItemList = new ArrayList<>(vendorProductList.size());
        List<Product> updatedProductSales = new ArrayList<>(vendorProductList.size());
        for(int i = 0 ; i< vendorProductList.size() ; i++){
            int quantity = quantities.get(0);
            VendorProduct vendorProduct = vendorProductList.get(i);
            vendorProduct.setStock(vendorProduct.getStock() - 1);
            Product product = vendorProduct.getProduct();
            product.setSalesCount(product.getSalesCount() + 1);
            updatedProductSales.add(product);
            orderItemList.add(OrderItem.builder()
                            .price(vendorProduct.getPrice())
                            .order(newOrder)
                            .product(product)
                            .quantity(quantity)
                            .vendor(vendorProduct.getVendor())
                            .status(OrderItemStatus.SHIPPED)
                    .build());
            newOrder.setTotalAmount(newOrder.getTotalAmount() + vendorProduct.getPrice());
        }

        try {
            orderRepository.save(newOrder);
            productRepository.saveAll(updatedProductSales);
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

        if(orderItem.getStatus() == OrderItemStatus.DELIVERED) throw new CustomBadRequestException("U can't change order status after it has been delivered ");

        orderItem.setStatus(orderItemStatusDto.getOrderItemStatus());

        try {
            if(orderItemStatusDto.getOrderItemStatus() == OrderItemStatus.CANCELLED && orderItem.getStatus() != OrderItemStatus.CANCELLED){
                VendorProduct vendorProduct = vendorProductRepository.findById(
                        orderItemStatusDto.getVendorProductId()
                ).orElseThrow(() -> new CustomNotFoundException("vendor product is not found"));
                Product product = vendorProduct.getProduct();
                product.setSalesCount(product.getSalesCount() - 1);
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

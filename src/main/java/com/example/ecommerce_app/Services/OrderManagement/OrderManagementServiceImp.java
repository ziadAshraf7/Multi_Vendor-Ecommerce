package com.example.ecommerce_app.Services.OrderManagement;

import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemBatchSavedDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemStatusDto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Entity.Enums.OrderItemStatus;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
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
import java.util.Objects;

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

    private final CartItemRepository cartItemRepository;


    @Transactional
    @Override
    public void makeOrder() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long authenticatedUserId = ( (AuthenticatedUserDto) authentication.getPrincipal()).getId();

        User customer = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new CustomNotFoundException("user is not found") );

        long cartId = customer.getCart().get(0).getId();


        Order newOrder = Order.builder().customer(customer).build();
        newOrder = orderRepository.save(newOrder);

        List<CartItem> cartItemList = cartItemRepository.findByCartId(cartId);

        if(cartItemList.isEmpty()) throw new CustomBadRequestException("cart is empty");

        List<OrderItem> orderItemList = new ArrayList<>(cartItemList.size());
        List<Product> updatedProductSales = new ArrayList<>(cartItemList.size());
        List<VendorProduct> updatedvendorProductList = new ArrayList<>(cartItemList.size());

        for(CartItem cartItem : cartItemList){
            VendorProduct vendorProduct = cartItem.getVendorProduct();
            if(vendorProduct.getStock() == 0) throw new CustomBadRequestException("Item ran out of Stock");
            vendorProduct.setStock(vendorProduct.getStock() - 1);
            Product product = vendorProduct.getProduct();
            product.setSalesCount(product.getSalesCount() + 1);
            updatedProductSales.add(product);
            updatedvendorProductList.add(vendorProduct);
            orderItemList.add(OrderItem.builder()
                            .price(vendorProduct.getPrice())
                            .order(newOrder)
                            .product(product)
                            .quantity(cartItem.getQuantity())
                            .vendor(vendorProduct.getVendor())
                            .status(OrderItemStatus.SHIPPED)
                    .build());
            newOrder.setTotalAmount(newOrder.getTotalAmount() + vendorProduct.getPrice());
        }

            orderRepository.save(newOrder);
            productRepository.saveAll(updatedProductSales);
            vendorProductRepository.saveAll(updatedvendorProductList);
            orderItemRepository.saveAll(orderItemList);
            cartItemRepository.deleteAllBYCartId(cartId);
    }


    @Transactional
    @Override
    public void changeOrderItemStatus(OrderItemStatusDto orderItemStatusDto ) {

        OrderItem orderItem = orderItemRepository.findById(orderItemStatusDto.getOrderItemId())
                .orElseThrow(() -> new CustomNotFoundException("order item is not found"));

        if(orderItem.getStatus() == OrderItemStatus.DELIVERED) throw new CustomBadRequestException("U can't change order status after it has been delivered ");

        orderItem.setStatus(orderItemStatusDto.getOrderItemStatus());

            if(orderItemStatusDto.getOrderItemStatus() == OrderItemStatus.CANCELLED && orderItem.getStatus() != OrderItemStatus.CANCELLED){
                VendorProduct vendorProduct = vendorProductRepository.findById(
                        orderItemStatusDto.getVendorProductId()
                ).orElseThrow(() -> new CustomNotFoundException("vendor product is not found"));
                Product product = vendorProduct.getProduct();
                product.setSalesCount(product.getSalesCount() - 1);
                vendorProduct.setStock(vendorProduct.getStock() + 1);
                vendorProductRepository.save(vendorProduct);
                productRepository.save(product);
            }

            orderItemRepository.save(orderItem);

    }


}

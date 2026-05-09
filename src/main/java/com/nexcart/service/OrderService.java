package com.nexcart.service;

import com.nexcart.Enum.ProductStatus;
import com.nexcart.dto.request.OrderRequestDto;
import com.nexcart.dto.response.OrderResponseDto;
import com.nexcart.exception.CardNotFoundException;
import com.nexcart.exception.CustomerNotFoundException;
import com.nexcart.exception.InsufficientQuantityException;
import com.nexcart.exception.ProductNotFoundException;
import com.nexcart.model.*;
import com.nexcart.repository.CardRepository;
import com.nexcart.repository.CustomerRepository;
import com.nexcart.repository.OrderRepository;
import com.nexcart.repository.ProductRepository;
import com.nexcart.transformer.ItemTransformer;
import com.nexcart.transformer.OrderTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    OrderRepository orderRepository;

    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        Customer customer = customerRepository.findByEmailId(orderRequestDto.getCustomerEmail());
        if(customer == null){
            throw new CustomerNotFoundException("customer doesn't exist");
        }
        Optional<Product> optionalProduct = productRepository.findById(orderRequestDto.getProductId());
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("product doesn't exist");
        }
        Card card = cardRepository.findByCardNo(orderRequestDto.getCardUsed());
        Date todayDate = new Date();
        if( card == null || card.getCvv()!= orderRequestDto.getCvv()  || todayDate.after(card.getValidTill())){
            throw new CardNotFoundException("invalid card");
        }

        Product product = optionalProduct.get();
        if(product.getAvailableQuantity() < orderRequestDto.getRequiredQuantity()){
            throw new InsufficientQuantityException("insufficient quantity available");
        }

        int newQuantity = product.getAvailableQuantity() - orderRequestDto.getRequiredQuantity();
        product.setAvailableQuantity(newQuantity);
        if(newQuantity == 0){
            product.setProductStatus(ProductStatus.OUT_OF_STOCK);
        }
        OrderEntity order = new OrderEntity();
        order.setOrderId(String.valueOf(UUID.randomUUID()));
        order.setCardUsed(orderRequestDto.getCardUsed());
        order.setOrderTotal(product.getPrice() * orderRequestDto.getRequiredQuantity());

        Item item = ItemTransformer.ItemRequestDtoToItem(orderRequestDto.getRequiredQuantity());
        item.setOrderEntity(order);
        item.setProduct(product);
        order.getItems().add(item);

        OrderEntity savedOrder = orderRepository.save(order);
        savedOrder.setCustomer(customer);

        product.getItems().add(savedOrder.getItems().get(0));
        customer.getOrders().add(savedOrder);

        productRepository.save(product);
        customerRepository.save(customer);

        return OrderTransformer.OrderToOrderResponseDto(savedOrder);

    }
}

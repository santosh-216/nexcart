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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    CardService cardService;

    @Autowired
    JavaMailSender javaMailSender;

    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        Customer customer = customerRepository.findByEmailId(orderRequestDto.getCustomerEmail());
        if(customer == null){
            throw new CustomerNotFoundException("customer doesn't exist");
        }
        Optional<Product> optionalProduct = productRepository.findById(orderRequestDto.getProductId());
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("product doesn't exist");
        }
        Card card = cardRepository.findByCardNo(orderRequestDto.getCardNo());
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
        order.setCardUsed(cardService.generateMaskedCard(orderRequestDto.getCardNo()));
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

        sendEmail(savedOrder);

        return OrderTransformer.OrderToOrderResponseDto(savedOrder);

    }



    public OrderEntity placeOrder(Cart cart, Card card) {

        OrderEntity order = new OrderEntity();
        order.setOrderId(String.valueOf(UUID.randomUUID()));
        order.setCardUsed(cardService.generateMaskedCard(card.getCardNo()));

        int orderTotal = 0;

        for(Item item : cart.getItems()){
            Product product = item.getProduct();
            if(product.getAvailableQuantity() < item.getRequiredQuantity()){
                throw new InsufficientQuantityException("Sorry! Insufficient quatity available for:"+product.getProductName());
            }

            int newQuantity = product.getAvailableQuantity() - item.getRequiredQuantity();
            product.setAvailableQuantity(newQuantity);
            if(newQuantity == 0){
                product.setProductStatus(ProductStatus.OUT_OF_STOCK);
            }
            orderTotal += product.getPrice() * item.getRequiredQuantity();
            item.setOrderEntity(order);
        }

        order.setOrderTotal(orderTotal);
        order.setItems(cart.getItems());
        order.setCustomer(cart.getCustomer());

        return order;
    }

    public void sendEmail(OrderEntity savedOrder) {
        SimpleMailMessage mail = new SimpleMailMessage();

        String text =
                "Dear " + savedOrder.getCustomer().getName() + ",\n\n" +

                        "Thank you for shopping with NexCart!\n\n" +

                        "Your order has been placed successfully.\n\n" +

                        "Order Details:\n" +
                        "Order ID : " + savedOrder.getOrderId() + "\n" +
                        "Order Date : " + savedOrder.getOrderDate() + "\n" +
                        "Total Amount : ₹" + savedOrder.getOrderTotal() + "\n" +
                        "Payment Method : " + savedOrder.getCardUsed() + "\n\n" +

                        "Your order is currently being processed and will be shipped soon.\n\n" +

                        "We appreciate your trust in NexCart.\n\n" +

                        "Regards,\n" +
                        "Team NexCart";

        mail.setTo(savedOrder.getCustomer().getEmailId());
        mail.setFrom("global.notify.center@gmail.com");
        mail.setSubject("Order Confirmation - NexCart");
        mail.setText(text);

        javaMailSender.send(mail);
    }
}

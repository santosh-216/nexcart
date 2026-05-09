package com.nexcart.service;

import com.nexcart.dto.request.CheckoutCartRequestDto;
import com.nexcart.dto.request.ItemRequestDto;
import com.nexcart.dto.response.CartResponseDto;
import com.nexcart.dto.response.OrderResponseDto;
import com.nexcart.exception.CardNotFoundException;
import com.nexcart.exception.CustomerNotFoundException;
import com.nexcart.exception.EmptyCartException;
import com.nexcart.model.*;
import com.nexcart.repository.*;
import com.nexcart.transformer.CartTransformer;
import com.nexcart.transformer.OrderTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class CartService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    public CartResponseDto addItemToCart(ItemRequestDto itemRequestDto, Item item){
        Customer customer = customerRepository.findByEmailId(itemRequestDto.getCustomerEmail());
        Product product = productRepository.findById(itemRequestDto.getProductId()).get();
        Cart cart = customer.getCart();
        cart.setCartTotal(cart.getCartTotal() + product.getPrice()* itemRequestDto.getRequiredQuantity());

        item.setCart(cart);
        item.setProduct(product);
        Item savedItem = itemRepository.save(item);

        cart.getItems().add(savedItem);
        product.getItems().add(savedItem);
        Cart savedCart =  cartRepository.save(cart);
        productRepository.save(product);

        return CartTransformer.CartToCartResponseDto(savedCart);
    }

    public OrderResponseDto placeOrder(CheckoutCartRequestDto checkoutCartRequestDto) {

        Customer customer = customerRepository.findByEmailId(checkoutCartRequestDto.getCustomerEmail());
        if(customer == null){
            throw new CustomerNotFoundException("customer doesn't exist");
        }

        Card card = cardRepository.findByCardNo(checkoutCartRequestDto.getCardNo());
        Date todayDate = new Date();

        if(card == null || card.getCvv()!=checkoutCartRequestDto.getCvv() || todayDate.after(card.getValidTill()) ){
            throw new CardNotFoundException("Invalid card");
        }

        Cart cart = customer.getCart();
        if(cart.getItems().isEmpty()){
            throw new EmptyCartException("Sorry! The cart is empty");
        }

        OrderEntity order = orderService.placeOrder(cart,card);
        resetCart(cart);

        OrderEntity savedOrder = orderRepository.save(order);

        return OrderTransformer.OrderToOrderResponseDto(savedOrder);
    }
    public void  resetCart(Cart cart){
        cart.setCartTotal(0);
        for(Item item : cart.getItems()){
            item.setCart(null);
        }
        cart.setItems(new ArrayList<>());
    }
}

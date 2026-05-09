package com.nexcart.service;

import com.nexcart.dto.request.ItemRequestDto;
import com.nexcart.dto.response.CartResponseDto;
import com.nexcart.model.Cart;
import com.nexcart.model.Customer;
import com.nexcart.model.Item;
import com.nexcart.model.Product;
import com.nexcart.repository.CartRepository;
import com.nexcart.repository.CustomerRepository;
import com.nexcart.repository.ItemRepository;
import com.nexcart.repository.ProductRepository;
import com.nexcart.transformer.CartTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

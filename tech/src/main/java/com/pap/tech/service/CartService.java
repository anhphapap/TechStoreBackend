package com.pap.tech.service;

import com.pap.tech.dto.request.CartItemRequest;
import com.pap.tech.dto.response.CartItemResponse;
import com.pap.tech.entity.Cart;
import com.pap.tech.entity.CartItem;
import com.pap.tech.entity.Product;
import com.pap.tech.entity.User;
import com.pap.tech.exception.AppException;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.mapper.CartItemMapper;
import com.pap.tech.repository.CartItemRepository;
import com.pap.tech.repository.CartRepository;
import com.pap.tech.repository.ProductRepository;
import com.pap.tech.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    CartRepository cartRepository;
    CartItemMapper cartItemMapper;

    public void addItem(String cartId, String productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new AppException(ErrorCode.UNKNOWN_ERROR));

        Product product =  productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.UNKNOWN_ERROR));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId).orElse(null);

        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
        }else{
            cartItem.setQuantity(quantity);
        }

        cartItemRepository.save(cartItem);
    }

    public List<CartItemResponse> getCart(){
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<CartItem> items = cartItemRepository.findCartItemsByCartId(user.getCart().getId());
        return items.stream().map(cartItemMapper::toCartItem).collect(Collectors.toList());
    }

    public List<CartItemResponse> mergeCart(List<CartItemRequest> cartItemRequests) {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Cart cart = user.getCart();

        for (CartItemRequest req : cartItemRequests) {
            Product product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            Optional<CartItem> opt = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

            CartItem cartItem;
            if (opt.isPresent()) {
                cartItem = opt.get();
                cartItem.setQuantity(cartItem.getQuantity() + req.getQuantity());
            } else {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(req.getQuantity());
            }

            cartItemRepository.save(cartItem);
        }

        List<CartItem> updatedItems = cartItemRepository.findCartItemsByCartId(cart.getId());

        return updatedItems.stream()
                .map(item -> CartItemResponse.builder()
                        .quantity(item.getQuantity())
                        .productName(item.getProduct().getName())
                        .productImage(item.getProduct().getImage())
                        .productPrice(item.getProduct().getPrice())
                        .productId(item.getProduct().getId())
                        .build())
                .toList();
    }

    public List<CartItemResponse> deleteCartItem(String productId) {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        CartItem ci = cartItemRepository.findByCartIdAndProductId(user.getCart().getId(), productId).orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItemRepository.deleteById(ci.getId());
        List<CartItem> items = cartItemRepository.findCartItemsByCartId(user.getCart().getId());
        return items.stream().map(cartItemMapper::toCartItem).collect(Collectors.toList());
    }

}

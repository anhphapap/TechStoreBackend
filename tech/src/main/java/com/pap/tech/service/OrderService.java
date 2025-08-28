package com.pap.tech.service;

import com.pap.tech.dto.request.OrderDetailRequest;
import com.pap.tech.dto.request.OrderRequest;
import com.pap.tech.dto.request.UpdateOrderRequest;
import com.pap.tech.entity.*;
import com.pap.tech.enums.PaymentStatus;
import com.pap.tech.exception.AppException;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.mapper.OrderDetailMapper;
import com.pap.tech.mapper.OrderMapper;
import com.pap.tech.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    AddressRepository addressRepository;
    UserRepository userRepository;
    OrderDetailRepository  orderDetailRepository;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderDetailMapper orderDetailMapper;
    ProductRepository productRepository;

    public void addOrder(OrderRequest request) {
        Address address = addressRepository.findById(request.getAddressId()).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Order order = orderMapper.toOrder(request);
        order.setAddress(address);
        order.setUser(user);
        orderRepository.save(order);

        for (OrderDetailRequest detailReq : request.getOrderDetails()) {
            OrderDetail detail = orderDetailMapper.toOrderDetail(detailReq);

            detail.setOrder(order);

            if (detailReq.getProductId() != null) {
                Product product = productRepository.findById(detailReq.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                detail.setProduct(product);
            }

            orderDetailRepository.save(detail);
        }
    }

    public void updateOrder(String vnpTxnref, String vnpPaydate, PaymentStatus status) {
        Order order = orderRepository.findOrderByVnpTxnref(vnpTxnref);
        order.setStatus(status);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime date = LocalDateTime.parse(vnpPaydate, formatter);
        order.setVnpPaydate(date);
        orderRepository.save(order);
    }
}

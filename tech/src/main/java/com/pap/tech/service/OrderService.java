package com.pap.tech.service;

import com.pap.tech.dto.request.OrderDetailRequest;
import com.pap.tech.dto.request.OrderRequest;
import com.pap.tech.dto.response.ListOrderResponse;
import com.pap.tech.dto.response.OrderDetailResponse;
import com.pap.tech.dto.response.OrderResponse;
import com.pap.tech.entity.*;
import com.pap.tech.enums.OrderStatus;
import com.pap.tech.exception.AppException;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.mapper.OrderDetailMapper;
import com.pap.tech.mapper.OrderMapper;
import com.pap.tech.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    int PAGE_SIZE = 5;
    AddressRepository addressRepository;
    UserRepository userRepository;
    OrderDetailRepository  orderDetailRepository;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderDetailMapper orderDetailMapper;
    ProductRepository productRepository;

    public String addOrder(OrderRequest request) {
        Address address = addressRepository.findById(request.getAddressId()).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Order order = orderMapper.toOrder(request);
        order.setAddress(address);
        order.setUser(user);
        System.out.println(order.getTotalamount());
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
        return order.getId();
    }

    public void updatePaymentOrder(String id, String vnpPaydate, OrderStatus status, Boolean paymentstatus) {
        Order order = orderRepository.findOrderById(id);
        order.setStatus(status);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime date = LocalDateTime.parse(vnpPaydate, formatter);
        order.setVnpPaydate(date);
        order.setPaymentstatus(paymentstatus);
        orderRepository.save(order);
    }

    public void updateRepaidVnpTxnRef(String id, String vnpTxnref) {
        Order order = orderRepository.findOrderById(id);
        order.setVnpTxnref(vnpTxnref);
        orderRepository.save(order);
    }

    public Page<ListOrderResponse> getOrders(String status, int page){
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        OrderStatus s = null;
        switch (status) {
            case "PAID":
                s = OrderStatus.PAID;
                break;
            case "CANCELLED":
                s = OrderStatus.CANCELLED;
                break;
            case "PENDING":
                s = OrderStatus.PENDING;
                break;
            case "SHIPPED":
                s = OrderStatus.SHIPPED;
                break;
            case "DELIVERED":
                s = OrderStatus.DELIVERED;
                break;
        }
        Page<Order> orders = orderRepository.findByUsernameAndOptionalStatus(name, s, pageable);
        Page<ListOrderResponse> responses = orders.map(order -> {
            ListOrderResponse res = new ListOrderResponse();
            res.setId(order.getId());
            res.setTotalamount(order.getTotalamount());
            res.setVnpTxnref(order.getVnpTxnref());
            res.setPaymentstatus(order.getPaymentstatus());
            res.setStatus(order.getStatus().name());
            List<OrderDetailResponse> details = orderDetailRepository.findByOrderId(order.getId()).stream()
                    .map(orderDetailMapper::toOrderDetailResponse)
                    .collect(Collectors.toList());
            res.setOrderDetails(details);
            return res;
        });
        return responses;
    }

    public OrderResponse getOrderDetail(String id) {
        Order order = orderRepository.findOrderById(id);
        OrderResponse res = orderMapper.toOrderResponse(order);
        List<OrderDetailResponse> detailResponses = orderDetailRepository.findByOrderId(order.getId()).stream()
                .map(orderDetailMapper::toOrderDetailResponse)
                .collect(Collectors.toList());
        res.setOrderDetails(detailResponses);
        return res;
    }

    public void updateOrderStatus(String id, String status) {
        Order order = orderRepository.findOrderById(id);
        OrderStatus s = null;
        switch (status) {
            case "CANCELLED":
                s = OrderStatus.CANCELLED;
                order.setCancelleddate(LocalDateTime.now());
                break;
            case "SHIPPED":
                s = OrderStatus.SHIPPED;
                order.setShippeddate(LocalDateTime.now());
                break;
            case "DELIVERED":
                s = OrderStatus.DELIVERED;
                order.setDelivereddate(LocalDateTime.now());
                break;
        }
        order.setStatus(s);
        orderRepository.save(order);
    }

    @PostAuthorize("returnObject.user.username == authentication.name")
    public Order getOrderByVnpTxnRef(String vnpTxnref) {
        return orderRepository.findOrderByVnpTxnref(vnpTxnref);
    }
}

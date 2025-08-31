package com.pap.tech.controller;

import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.dto.response.PaymentResponse;
import com.pap.tech.entity.Order;
import com.pap.tech.enums.OrderStatus;
import com.pap.tech.service.OrderService;
import com.pap.tech.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Enumeration;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;
    OrderService orderService;

    @GetMapping("/vn-pay")
    public ApiResponse<PaymentResponse> pay(HttpServletRequest request) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.createVnPayPayment(request))
                .build();
    }
    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String status = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");
        String payDateStr = request.getParameter("vnp_PayDate");
        Order order = orderService.getOrderByVnpTxnRef(txnRef);

        String redirectUrl = "http://localhost:5173/payment-result";

        if (status.equals("00")) {
            orderService.updatePaymentOrder(order.getId(), payDateStr , OrderStatus.PAID, Boolean.TRUE);
            redirectUrl += "?status=success&orderId=" + order.getId();
        } else {
            orderService.updatePaymentOrder(order.getId(), payDateStr, OrderStatus.PENDING, Boolean.FALSE);
            redirectUrl += "?status=fail&orderId=" + order.getId();
        }

        response.sendRedirect(redirectUrl);
    }
}

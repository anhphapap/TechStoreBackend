package com.pap.tech.controller;

import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.dto.response.PaymentResponse;
import com.pap.tech.entity.Order;
import com.pap.tech.enums.PaymentStatus;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.service.OrderService;
import com.pap.tech.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            System.out.println(paramName + " = " + paramValue);
        }

        String redirectUrl = "http://localhost:3000/payment-result";

        if (status.equals("00")) {
            orderService.updateOrder(txnRef, payDateStr , PaymentStatus.PAID);
            redirectUrl += "?status=success&orderId=" + txnRef;
        } else {
            orderService.updateOrder(txnRef, payDateStr, PaymentStatus.CANCELLED);
            redirectUrl += "?status=fail&orderId=" + txnRef;
        }

        response.sendRedirect(redirectUrl);
    }
}

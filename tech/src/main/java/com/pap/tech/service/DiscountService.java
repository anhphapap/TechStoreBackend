package com.pap.tech.service;

import com.pap.tech.dto.request.DiscountRequest;
import com.pap.tech.entity.Discount;
import com.pap.tech.exception.AppException;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.mapper.DiscountMapper;
import com.pap.tech.repository.DiscountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiscountService {
    DiscountRepository discountRepository;
    DiscountMapper  discountMapper;

    public BigDecimal applyDiscount(String code, BigDecimal orderTotal) {
        Discount discount = discountRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));

        if ((discount.getExpiredat() != null && discount.getExpiredat().isBefore(LocalDateTime.now()))
                || !discount.getActive()) {
            throw new AppException(ErrorCode.EXPIRED_DISCOUNT);
        }

        if (discount.getMinorder() != null && orderTotal.compareTo(discount.getMinorder()) < 0) {
            throw new AppException(ErrorCode.DISCOUNT_CAN_NOT_APPLIED);
        }

        BigDecimal discountPercent = BigDecimal.valueOf(discount.getDiscountpercent());
        BigDecimal discountAmount = orderTotal.multiply(discountPercent)
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

        if (discount.getMaxdiscount() != null) {
            discountAmount = discountAmount.min(discount.getMaxdiscount());
        }

        return discountAmount;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Discount> getDiscounts() {
        return discountRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Discount getDiscount(String id) {
        return discountRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void activeDiscount(String id) {
        Discount discount = discountRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
        discount.setActive(!discount.getActive());
        discountRepository.save(discount);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void addDiscount(DiscountRequest request) {
        Discount discount = discountMapper.toDiscount(request);
        discount.setActive(true);
        discountRepository.save(discount);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateDiscount(String id, DiscountRequest request) {
        Discount discount = discountRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
        discountMapper.updateDiscount(discount, request);
        discountRepository.save(discount);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDiscount(String id) {
        discountRepository.deleteDiscountById(id);
    }
}

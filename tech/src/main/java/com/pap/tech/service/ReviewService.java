package com.pap.tech.service;

import com.pap.tech.dto.request.OrderRequest;
import com.pap.tech.dto.request.ReviewRequest;
import com.pap.tech.dto.response.ReviewResponse;
import com.pap.tech.entity.Product;
import com.pap.tech.entity.Review;
import com.pap.tech.entity.User;
import com.pap.tech.exception.AppException;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.repository.ProductRepository;
import com.pap.tech.repository.ReviewRepository;
import com.pap.tech.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    UserRepository userRepository;
    ProductRepository productRepository;
    ReviewRepository reviewRepository;

    @Transactional
    public void addReview(ReviewRequest request, String productId) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        review.setCreatedat(LocalDateTime.now());
        reviewRepository.save(review);

        int oldCount = product.getRatingCount();
        double oldAvg = product.getAvgRating();

        int newCount = oldCount + 1;
        double newAvg = ((oldAvg * oldCount) + request.getRating()) / newCount;

        product.setRatingCount(newCount);
        product.setAvgRating(newAvg);

        productRepository.save(product);
    }

    public Page<ReviewResponse> getReviews(String productId, int page, int size) {
        Page<Review> reviews = reviewRepository.findByProduct_Id(productId, PageRequest.of(page-1, size, Sort.by("createdat").descending()));
        return reviews.map(r -> ReviewResponse.builder()
                .id(r.getId())
                .rating(r.getRating())
                .comment(r.getComment())
                .username(r.getUser().getFullname())
                .createdAt(r.getCreatedat())
                .build());
    }

    public Map<String, Object> getReviewStats(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<Object[]> starCounts = reviewRepository.countStarsByProductId(productId);

        Map<Integer, Long> stars = new HashMap<>();
        for (Object[] row : starCounts) {
            int rating = (Integer) row[0];
            long count = (Long) row[1];
            stars.put(rating, count);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("avgRating", product.getAvgRating());
        result.put("ratingCount", product.getRatingCount());
        result.put("stars", stars);

        return result;
    }
}

package com.full_stack.trendsetter.service;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.model.Review;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest req , User user ) throws ProductException;

    public List<Review> getAllReview (Long productId);
}

package com.full_stack.trendsetter.service;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.model.Rating;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.request.RatingRequest;

import java.util.List;

public interface RatingService {

    public Rating createRating(RatingRequest req , User user) throws ProductException;

    public List<Rating> getProductRating (Long productId);

}

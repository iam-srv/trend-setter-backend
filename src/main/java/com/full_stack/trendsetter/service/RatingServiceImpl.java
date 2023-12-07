package com.full_stack.trendsetter.service;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.model.Product;
import com.full_stack.trendsetter.model.Rating;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.repository.RatingRepository;
import com.full_stack.trendsetter.request.RatingRequest;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductService productService;

    @Override
    public Rating createRating(RatingRequest req, User user) throws ProductException {
         Product product = productService.findProductById(req.getProductId());
         Rating rating = new Rating();
         rating.setProduct(product);
         rating.setUser(user);
         rating.setRating(req.getRating());
         rating.setCreatedAt(LocalDateTime.now());
         return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductRating(Long productId) {
        return ratingRepository.getAllProductRating(productId);
    }
}

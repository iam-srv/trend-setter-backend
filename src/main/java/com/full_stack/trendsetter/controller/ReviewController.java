package com.full_stack.trendsetter.controller;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.exception.UserException;
import com.full_stack.trendsetter.model.Review;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.request.ReviewRequest;
import com.full_stack.trendsetter.service.ReviewService;
import com.full_stack.trendsetter.service.UserService;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req ,
                                               @RequestHeader("Authorization") String jwt) throws UserException , ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(req , user);

        return new ResponseEntity<>(review , HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductsReview(@PathVariable Long productId) throws UserException , ProductException {
        List<Review> reviews = reviewService.getAllReview(productId);
        return new ResponseEntity<>(reviews, HttpStatus.CREATED);
    }

}

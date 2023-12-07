package com.full_stack.trendsetter.controller;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.exception.UserException;
import com.full_stack.trendsetter.model.Rating;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.request.RatingRequest;
import com.full_stack.trendsetter.service.RatingService;
import com.full_stack.trendsetter.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req ,
                                               @RequestHeader("Authorization") String jwt) throws UserException , ProductException {

        User user = userService.findUserProfileByJwt(jwt);
        Rating rating = ratingService.createRating(req ,user);
        return new ResponseEntity<Rating>(rating , HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductsRating(@PathVariable Long productId ,
                                                          @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user  = userService.findUserProfileByJwt(jwt);
        List<Rating> ratings = ratingService.getProductRating(productId);
        return new ResponseEntity<>(ratings, HttpStatus.CREATED);
    }
}


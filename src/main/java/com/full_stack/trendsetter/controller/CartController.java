package com.full_stack.trendsetter.controller;

import com.full_stack.trendsetter.exception.ProductException;
import com.full_stack.trendsetter.exception.UserException;
import com.full_stack.trendsetter.model.Cart;
import com.full_stack.trendsetter.model.User;
import com.full_stack.trendsetter.request.AddItemRequest;
import com.full_stack.trendsetter.response.ApiResponse;
import com.full_stack.trendsetter.service.CartService;
import com.full_stack.trendsetter.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzTransactionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/") //
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add") //
    public ResponseEntity<Cart> addItemToCart(@RequestBody AddItemRequest req ,
                                                     @RequestHeader("Authorization") String jwt) throws UserException , ProductException {
        User user = userService.findUserProfileByJwt(jwt);

        Cart cart = cartService.addCartItem(user.getId() , req);

        ApiResponse res = new ApiResponse();
        res.setMessage("Item added to cart");
        res.setStatus(true);
        return new ResponseEntity<>(cart , HttpStatus.OK);
    }


}
